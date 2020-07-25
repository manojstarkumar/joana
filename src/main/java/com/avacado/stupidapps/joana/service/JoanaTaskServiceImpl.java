package com.avacado.stupidapps.joana.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avacado.stupidapps.joana.domain.JoanaExecutionMode;
import com.avacado.stupidapps.joana.domain.JoanaReviewStates;
import com.avacado.stupidapps.joana.domain.JoanaStates;
import com.avacado.stupidapps.joana.domain.JoanaUser;
import com.avacado.stupidapps.joana.domain.task.JoanaTask;
import com.avacado.stupidapps.joana.domain.task.JoanaTaskBase.JoanaTaskAction;
import com.avacado.stupidapps.joana.domain.task.JoanaTaskExecution;
import com.avacado.stupidapps.joana.exceptions.JoanaException;
import com.avacado.stupidapps.joana.protocol.request.JoanaCreateTaskRequest;
import com.avacado.stupidapps.joana.protocol.request.JoanaTaskExecutionUpdateRequest;
import com.avacado.stupidapps.joana.repository.JoanaTaskExecutionRepository;
import com.avacado.stupidapps.joana.repository.JoanaTaskRepository;
import com.avacado.stupidapps.joana.repository.JoanaUserRepository;
import com.avacado.stupidapps.joana.service.interfaces.JoanaQueueService;
import com.avacado.stupidapps.joana.service.interfaces.JoanaTaskService;

@Service
public class JoanaTaskServiceImpl implements JoanaTaskService {

    @Autowired
    private JoanaTaskRepository joanaTaskRepository;

    @Autowired
    private JoanaTaskExecutionRepository joanaTaskExecutionRepository;

    @Autowired
    private JoanaQueueService joanaQueueService;

    @Autowired
    private JoanaUserRepository joanaUserRepository;

    private Logger logger = LoggerFactory.getLogger(JoanaTaskServiceImpl.class);

    @Transactional
    @Override
    public JoanaTask createTask(JoanaUser currentUser, JoanaCreateTaskRequest createTaskRequest) {
	JoanaTask task = new JoanaTask();
	task.setOwner(currentUser.getEmail());
	task.setName(createTaskRequest.getName());
	createTaskRequest.getActions().forEach(createAction -> {
	    JoanaTaskAction action = new JoanaTaskAction();
	    action.setPosition(createAction.getPosition());
	    action.setName(createAction.getName());
	    task.addAction(action);
	});
	createTaskRequest.getReviewers().forEach(email -> {
	    JoanaUser user = joanaUserRepository.findByEmail(email);
	    if (user == null)
		throw new JoanaException("Invalid reviewer " + email, HttpStatus.BAD_REQUEST);
	    task.addReviewer(user);

	});
	JoanaTask persistedTask = joanaTaskRepository.save(task);
	currentUser.addTasksOwned(persistedTask.getId());
	joanaUserRepository.save(currentUser);
	return persistedTask;

    }

    @Transactional(rollbackFor = NullPointerException.class)
    @Override
    public JoanaTaskExecution startTaskExecution(String taskId) {
	Optional<JoanaTaskExecution> persistedExecution = joanaTaskExecutionRepository.findById(taskId);
	if (!persistedExecution.isPresent()) {
	    throw new JoanaException("Invalid taskid " + taskId, HttpStatus.BAD_REQUEST);
	}
	persistedExecution.get().setState(JoanaStates.PROGRESS);
	joanaQueueService.queueTaskForProcessing(joanaTaskExecutionRepository.save(persistedExecution.get()));
	return persistedExecution.get();
    }

    @Transactional
    @Override
    public JoanaTaskExecution updateExecutionTaskStatus(JoanaUser currentUser, String taskId, JoanaReviewStates state,
	    JoanaTaskExecutionUpdateRequest joanaTaskExecutionUpdateRequest) {
	Optional<JoanaTaskExecution> task = joanaTaskExecutionRepository.findById(taskId);
	if (!task.isPresent()) {
	    throw new JoanaException("Invalid taskid " + taskId, HttpStatus.BAD_REQUEST);
	}
	if (state != JoanaReviewStates.NONE) {
	    throwExceptionIfTaskExecutionStateTransitionNotAllowed(task.get());
	    if (!task.get().getReviewers().stream()
		    .anyMatch(reviewer -> reviewer.getEmail().equals(currentUser.getEmail()))) {
		logger.error("Non reviewer update for task {}. User {}", task.get().getName(), currentUser.getName());
		throw new JoanaException("Invalid action", HttpStatus.FORBIDDEN);
	    }
	}

	if (joanaTaskExecutionUpdateRequest != null) {
	    if (joanaTaskExecutionUpdateRequest.getUpdateRequestTaskChanges() != null) {
		List<JoanaTaskAction> taskActions = task.get().getActions();
		joanaTaskExecutionUpdateRequest.getUpdateRequestTaskChanges().parallelStream().forEach(change -> {
		    Optional<JoanaTaskAction> matchedAction = taskActions.stream()
			    .filter(action -> action.getPosition() == change.getPosition()).findFirst();
		    if (!matchedAction.isPresent() || !matchedAction.get().getName().equals(change.getTaskName()))
			throw new JoanaException("Invalid action", HttpStatus.BAD_REQUEST);
		    matchedAction.get().setState(change.getState());
		    task.get().addComment("AUTO-" + currentUser.getName(),
			    change.getState().getJoanaReviewDisplayState() + ": " + matchedAction.get().getName());
		});
	    }
	    if (joanaTaskExecutionUpdateRequest.getComments() != null) {
		joanaTaskExecutionUpdateRequest.getComments().parallelStream().forEach(comment -> {
		    task.get().addComment(currentUser.getName(), comment);
		});
	    }
	}
	boolean sendNotification = false;
	if (state != JoanaReviewStates.NONE) {
	    sendNotification = updateReviewAndTaskStatusAndGetNotificationStatus(task.get(), currentUser, state);
	}

	JoanaTaskExecution persistedExecution = joanaTaskExecutionRepository.save(task.get());
	if (sendNotification)
	    joanaQueueService.queueTaskForProcessing(persistedExecution);
	return persistedExecution;
    }

    private boolean updateReviewAndTaskStatusAndGetNotificationStatus(JoanaTaskExecution task, JoanaUser currentUser,
	    JoanaReviewStates state) {
	task.getReviewers().parallelStream().filter(reviewer -> reviewer.getEmail().equals(currentUser.getEmail()))
		.findFirst().get().setReviewState(state);
	task.addComment("AUTO-" + currentUser.getName(), "Task: " + state.getJoanaReviewDisplayState());

	boolean sendNotification = false;
	if (state == JoanaReviewStates.REJECTED) {
	    sendNotification = true;
	    task.setState(JoanaStates.FAILED);
	    task.setCompletedAt(new Date());
	    task.addComment("AUTO-SYS", "Overall status: " + JoanaStates.FAILED.getDisplayName());
	}

	if (state == JoanaReviewStates.APPROVED || state == JoanaReviewStates.WARNING) {
	    if (!task.getReviewers().parallelStream()
		    .anyMatch(reviewer -> reviewer.getReviewState() != JoanaReviewStates.APPROVED
			    && reviewer.getReviewState() != JoanaReviewStates.WARNING)) {
		sendNotification = true;
		task.setState(JoanaStates.COMPLETE);
		task.setCompletedAt(new Date());
		task.addComment("AUTO-SYS", "Overall status: " + JoanaStates.COMPLETE.getDisplayName());
	    }
	}

	return sendNotification;
    }

    private void throwExceptionIfTaskExecutionStateTransitionNotAllowed(JoanaTaskExecution joanaTaskExecution) {
	if (joanaTaskExecution.getState() == JoanaStates.COMPLETE || joanaTaskExecution.getState() == JoanaStates.FAILED
		|| joanaTaskExecution.getState() == JoanaStates.ABORT) {
	    throw new JoanaException("Invalid operation. Task in final state", HttpStatus.FORBIDDEN);
	}

    }

    @Transactional
    @Override
    public JoanaTaskExecution createTaskExecution(JoanaUser currentUser, String taskId, JoanaStates taskState,
	    JoanaExecutionMode executionMode) {
	Optional<JoanaTask> task = joanaTaskRepository.findById(taskId);
	if (!task.isPresent()) {
	    throw new JoanaException("Invalid taskid " + taskId, HttpStatus.BAD_REQUEST);
	}
	JoanaTaskExecution taskExecution = JoanaTaskExecution.fromTask(task.get());
	taskExecution.setStartedAt(new Date());
	taskExecution.setState(taskState);
	taskExecution.setExecutionMode(executionMode);
	taskExecution.setTriggeredBy(JoanaTaskExecution.JoanaTaskInitiator.build().withName(currentUser.getName())
		.withInitiatorId(currentUser.getEmail()));
	return joanaTaskExecutionRepository.save(taskExecution);

    }

    @Override
    public JoanaTaskExecution getTaskExecution(String taskId) {
	Optional<JoanaTaskExecution> task = joanaTaskExecutionRepository.findById(taskId);
	if (!task.isPresent()) {
	    throw new JoanaException("Task not found " + taskId, HttpStatus.NOT_FOUND);
	}
	return task.get();
    }

    @Override
    public JoanaTask getTask(String taskId) {
	Optional<JoanaTask> task = joanaTaskRepository.findById(taskId);
	if (!task.isPresent())
	    throw new JoanaException("Task not found " + taskId, HttpStatus.NOT_FOUND);
	return task.get();
    }

    @Transactional
    @Override
    public void deleteTask(JoanaUser currentUser, String taskId) {
	Optional<JoanaTask> task = joanaTaskRepository.findById(taskId);
	if (!task.isPresent())
	    throw new JoanaException("Task not found " + taskId, HttpStatus.NOT_FOUND);
	if (!task.get().getOwner().equals(currentUser.getEmail())) {
	    logger.error("Non owner delete for task {}. User {}", task.get().getName(), currentUser.getName());
	    throw new JoanaException("Invalid operation", HttpStatus.FORBIDDEN);
	}
	joanaTaskRepository.delete(task.get());
	currentUser.removeTasksActionsNeeded(taskId);
	joanaUserRepository.save(currentUser);
    }

    @Transactional
    @Override
    public JoanaTask updateTask(JoanaUser currentUser, String taskId, JoanaCreateTaskRequest createTaskRequest) {
	Optional<JoanaTask> task = joanaTaskRepository.findById(taskId);
	if (!task.isPresent())
	    throw new JoanaException("Task not found " + taskId, HttpStatus.NOT_FOUND);
	if (!task.get().getOwner().equals(currentUser.getEmail())) {
	    logger.error("Non owner update for task {}. User {}", task.get().getName(), currentUser.getName());
	    throw new JoanaException("Invalid operation", HttpStatus.FORBIDDEN);
	}

	task.get().setName(createTaskRequest.getName());
	createTaskRequest.getActions().forEach(createAction -> {
	    JoanaTaskAction action = new JoanaTaskAction();
	    action.setPosition(createAction.getPosition());
	    action.setName(createAction.getName());
	    task.get().addAction(action);
	});
	createTaskRequest.getReviewers().forEach(email -> {
	    JoanaUser user = joanaUserRepository.findByEmail(email);
	    if (user == null)
		throw new JoanaException("Invalid reviewer " + email, HttpStatus.BAD_REQUEST);
	    task.get().addReviewer(user);

	});

	return joanaTaskRepository.save(task.get());
    }

    @Override
    public List<JoanaTaskExecution> getPendingTasks(JoanaUser currentUser) {
	List<JoanaTaskExecution> pendingTasks = new ArrayList<>();
	if(currentUser.getTasksActionsNeeded() != null && currentUser.getTasksActionsNeeded().size() > 0) {
	    joanaTaskExecutionRepository.findAllById(currentUser.getTasksActionsNeeded()).forEach(pendingTasks::add);
	}
	return pendingTasks;
    }

}

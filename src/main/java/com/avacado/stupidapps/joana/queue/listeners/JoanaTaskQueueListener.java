package com.avacado.stupidapps.joana.queue.listeners;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.avacado.stupidapps.joana.domain.JoanaExecutionHistory;
import com.avacado.stupidapps.joana.domain.JoanaExecutionMode;
import com.avacado.stupidapps.joana.domain.JoanaStates;
import com.avacado.stupidapps.joana.domain.JoanaUser;
import com.avacado.stupidapps.joana.domain.notifiers.JoanaNotifier;
import com.avacado.stupidapps.joana.domain.pipe.JoanaPipeBase.JoanaPipeStep;
import com.avacado.stupidapps.joana.domain.pipe.JoanaPipeExecution;
import com.avacado.stupidapps.joana.domain.task.JoanaTask;
import com.avacado.stupidapps.joana.domain.task.JoanaTaskExecution;
import com.avacado.stupidapps.joana.repository.JoanaPipeExecutionRepository;
import com.avacado.stupidapps.joana.repository.JoanaTaskExecutionRepository;
import com.avacado.stupidapps.joana.repository.JoanaTaskRepository;
import com.avacado.stupidapps.joana.repository.JoanaUserRepository;
import com.avacado.stupidapps.joana.service.interfaces.JoanaTaskService;
import com.avacado.stupidapps.joana.utils.JoanaConstants;
import com.avacado.stupidapps.joana.utils.RequestUtils;

@Component
@RabbitListener(queues = "${joana.task.rabbit.queue}")
public class JoanaTaskQueueListener {

    private Logger logger = LoggerFactory.getLogger(JoanaTaskQueueListener.class);

    @Autowired
    private JoanaTaskRepository joanaTaskRepository;

    @Autowired
    private JoanaTaskExecutionRepository joanaTaskExecutionRepository;

    @Autowired
    private JoanaPipeExecutionRepository joanaPipeExecutionRepository;

    @Autowired
    private JoanaUserRepository joanaUserRepository;

    @Autowired
    private JoanaTaskService joanaTaskService;
    
    @Autowired
    private List<JoanaNotifier> notifiers;
    
    private ExecutorService executors = Executors.newCachedThreadPool();

    @Transactional
    @RabbitHandler
    public void processTaskExecution(@Payload JoanaTaskExecution joanaTaskExecution,
	    @Headers Map<String, Object> headers) {
	if (headers.get(JoanaConstants.RMQ_DB_VALUE_HEADER) != null) {
	    RequestUtils.setDatabaseName((String) headers.get(JoanaConstants.RMQ_DB_VALUE_HEADER));
	}
	notifyReviewersAndUpdatePendingTasks(joanaTaskExecution);

	logger.debug("Updating status of Parent " + joanaTaskExecution.getParentTask());

	JoanaTask joanaTask = joanaTaskRepository.findById(joanaTaskExecution.getParentTask()).get();
	updateHistoryRecord(joanaTask, joanaTaskExecution);

	if (joanaTaskExecution.getExecutionMode() == JoanaExecutionMode.PIPE) {
	    updateTaskInfoInPipe(joanaTaskExecution);
	}

	joanaTaskRepository.save(joanaTask);

    }

    private void updateHistoryRecord(JoanaTask joanaTask, JoanaTaskExecution joanaTaskExecution) {
	if (joanaTask.getHistory() == null) {
	    joanaTask.addExecutionHistory(JoanaExecutionHistory.build().withExecutionId(joanaTaskExecution.getId())
		    .withExecutionStatus(joanaTaskExecution.getState()).withExecutionDate(new Date()));
	    return;
	}
	Optional<JoanaExecutionHistory> history = joanaTask.getHistory().parallelStream()
		.filter(matchedHistory -> matchedHistory.getExecutionId().equals(joanaTaskExecution.getId()))
		.findFirst();
	if (!history.isPresent()) {
	    if (joanaTask.getHistory().size() > 50) {
		logger.debug("Delete oldest execution from task {} {}", joanaTask.getId(), joanaTask.getName());
		Collections.sort(joanaTask.getHistory());
		joanaTask.getHistory().remove(0);
	    }
	    joanaTask.addExecutionHistory(JoanaExecutionHistory.build().withExecutionId(joanaTaskExecution.getId())
		    .withExecutionStatus(joanaTaskExecution.getState()).withExecutionDate(new Date()));
	} else {
	    history.get().setExecutionStatus(joanaTaskExecution.getState());
	}
    }

    private void updateTaskInfoInPipe(JoanaTaskExecution joanaTaskExecution) {
	JoanaPipeExecution pipe = joanaPipeExecutionRepository
		.findById(joanaTaskExecution.getTriggeredBy().getInitiatorId()).get();

	JoanaPipeStep currentStep = pipe.getPipeSteps().stream()
		.filter(step -> step.getTaskId().equals(joanaTaskExecution.getId())).findFirst().get();
	currentStep.setStepState(joanaTaskExecution.getState());
	pipe.setCurrentStep(currentStep.getPosition());

	if (joanaTaskExecution.getState() == JoanaStates.COMPLETE) {
	    if (currentStep.getPosition() == pipe.getPipeSteps().size()) {
		pipe.setState(joanaTaskExecution.getState());
		pipe.setCompletedAt(new Date());
	    } else {
		joanaTaskService.startTaskExecution(
			pipe.getPipeSteps().stream().filter(step -> step.getPosition() == currentStep.getPosition() + 1)
				.findFirst().get().getTaskId());
	    }
	}

	if (joanaTaskExecution.getState() == JoanaStates.FAILED || joanaTaskExecution.getState() == JoanaStates.ABORT) {
	    pipe.setState(joanaTaskExecution.getState());
	    if (currentStep.getPosition() != pipe.getPipeSteps().size()) {
		pipe.getPipeSteps().parallelStream().filter(step -> step.getPosition() > currentStep.getPosition())
			.forEach(matchedStep -> {
			    joanaTaskExecutionRepository.deleteById(matchedStep.getTaskId());
			    matchedStep.setStepState(JoanaStates.UPSTREAMFAIL);
			});
	    }
	}

	String notificationText = String.format("FYI/A, %s stage %d/%d. Task %s : %s. Pipeline : %s", pipe.getPipeName(),
		currentStep.getPosition(), pipe.getPipeSteps().size(), joanaTaskExecution.getName(),
		joanaTaskExecution.getState().getDisplayName(), pipe.getState().getDisplayName());
	
	notifiers.forEach(notifier -> {
	    executors.execute(() -> notifier.notify(pipe.getPipeWatchers(), notificationText));
	});

	joanaPipeExecutionRepository.save(pipe);
    }

    private void notifyReviewersAndUpdatePendingTasks(JoanaTaskExecution joanaTaskExecution) {
	logger.debug("Notifying all reviewers");
	String notificationText = String.format("FYI/A, Task %s : %s", joanaTaskExecution.getName(),
		joanaTaskExecution.getState().getDisplayName());
	final List<String> peopleToNotify = Stream
		.concat(Stream.of(joanaTaskExecution.getTriggeredBy().getInitiatorId()),
			joanaTaskExecution.getReviewers().stream().map(reviewer -> reviewer.getEmail()))
		.distinct().collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
	
	executors.execute(() -> updateUserPendingActions(joanaTaskExecution));
	
	notifiers.forEach(notifier -> {
	    executors.execute(() -> notifier.notify(peopleToNotify, notificationText));
	});

	joanaTaskExecution.addComment("AUTO-SYS",
		String.format("%s notification sent", joanaTaskExecution.getState().getDisplayName()));
	joanaTaskExecutionRepository.save(joanaTaskExecution);

    }

    private void updateUserPendingActions(JoanaTaskExecution joanaTaskExecution) {
	joanaTaskExecution.getReviewers().stream().forEach(reviewer -> {
	    JoanaUser user = joanaUserRepository.findByEmail(reviewer.getEmail());
	    if (user != null) {
		if (joanaTaskExecution.getState() == JoanaStates.PROGRESS) {
		    user.addTasksActionsNeeded(joanaTaskExecution.getId());
		} else {
		    user.removeTasksActionsNeeded(joanaTaskExecution.getId());
		}
		joanaUserRepository.save(user);
	    }
	});
    }

}

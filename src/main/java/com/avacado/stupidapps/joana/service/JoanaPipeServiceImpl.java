package com.avacado.stupidapps.joana.service;

import java.util.Collections;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.avacado.stupidapps.joana.domain.JoanaStates;
import com.avacado.stupidapps.joana.domain.JoanaUser;
import com.avacado.stupidapps.joana.domain.pipe.JoanaPipe;
import com.avacado.stupidapps.joana.domain.pipe.JoanaPipeExecution;
import com.avacado.stupidapps.joana.domain.task.JoanaTask;
import com.avacado.stupidapps.joana.exceptions.JoanaException;
import com.avacado.stupidapps.joana.protocol.request.JoanaCreatePipeRequest;
import com.avacado.stupidapps.joana.repository.JoanaPipeExecutionRepository;
import com.avacado.stupidapps.joana.repository.JoanaPipeRepository;
import com.avacado.stupidapps.joana.repository.JoanaTaskRepository;
import com.avacado.stupidapps.joana.service.interfaces.JoanaPipeService;
import com.avacado.stupidapps.joana.service.interfaces.JoanaQueueService;

@Service
public class JoanaPipeServiceImpl implements JoanaPipeService {

    @Autowired
    private JoanaPipeRepository joanaPipeRepository;

    @Autowired
    private JoanaPipeExecutionRepository joanaPipeExecutionRepository;

    @Autowired
    private JoanaTaskRepository joanaTaskRepository;

    @Autowired
    private JoanaQueueService joanaQueueService;

    private Logger logger = LoggerFactory.getLogger(JoanaPipeServiceImpl.class);

    @Override
    public JoanaPipeExecution startPipeExecution(JoanaUser currentUser, String pipeId) {
	Optional<JoanaPipe> pipe = joanaPipeRepository.findById(pipeId);
	if (!pipe.isPresent()) {
	    throw new JoanaException("Pipe not found " + pipeId, HttpStatus.NOT_FOUND);
	}
	JoanaPipeExecution pipeExecution = new JoanaPipeExecution();
	pipeExecution.setParentPipe(pipe.get().getId());
	pipeExecution.setState(JoanaStates.SCHEDULED);
	pipeExecution.setTriggeredBy(currentUser.getName());
	pipeExecution.setPipeName(pipe.get().getPipeName());
	pipe.get().getPipeWatchers().parallelStream().forEach(watcher -> pipeExecution.addPipeWatchers(watcher));
	if(!pipe.get().getPipeWatchers().contains(currentUser.getEmail()))
	    pipe.get().addPipeWatchers(currentUser.getEmail());
	JoanaPipeExecution persistedExecution = joanaPipeExecutionRepository.save(pipeExecution);
	joanaQueueService.queuePipeForProcessing(persistedExecution);
	return persistedExecution;
    }

    @Override
    public JoanaPipeExecution getPipeExecution(String pipeId) {
	Optional<JoanaPipeExecution> pipe = joanaPipeExecutionRepository.findById(pipeId);
	if (!pipe.isPresent()) {
	    throw new JoanaException("Pipe execution not found " + pipeId, HttpStatus.NOT_FOUND);
	}
	return pipe.get();
    }

    @Override
    public void deletePipe(JoanaUser currentUser, String pipeId) {
	Optional<JoanaPipe> pipe = joanaPipeRepository.findById(pipeId);
	if (!pipe.isPresent()) {
	    throw new JoanaException("Pipe not found " + pipeId, HttpStatus.NOT_FOUND);
	}
	if (!pipe.get().getOwner().equals(currentUser.getEmail())) {
	    logger.error("Non owner delete for pipe {}. User {}", pipe.get().getPipeName(), currentUser.getName());
	    throw new JoanaException("Invalid action", HttpStatus.FORBIDDEN);
	}
	joanaPipeRepository.delete(pipe.get());
    }

    @Override
    public JoanaPipe getPipe(String pipeId) {
	Optional<JoanaPipe> pipe = joanaPipeRepository.findById(pipeId);
	if (!pipe.isPresent()) {
	    throw new JoanaException("Pipe execution not found " + pipeId, HttpStatus.NOT_FOUND);
	}
	return pipe.get();
    }

    @Override
    public JoanaPipe createPipe(JoanaUser currentUser, JoanaCreatePipeRequest createPipeRequest) {
	JoanaPipe pipe = new JoanaPipe();
	pipe.setOwner(currentUser.getEmail());
	pipe.setPipeName(createPipeRequest.getPipeName());
	pipe.addPipeWatchers(currentUser.getEmail());
	Collections.sort(createPipeRequest.getPipeSteps());
	createPipeRequest.getPipeSteps().stream().forEach(step -> {
	    Optional<JoanaTask> task = joanaTaskRepository.findById(step.getStepId());
	    if (!task.isPresent()) {
		throw new JoanaException("Invalid step id in pipe request " + step.getStepId(), HttpStatus.BAD_REQUEST);
	    }
	    pipe.addPipeStep(task.get().getId(), task.get().getName());
	});

	return joanaPipeRepository.save(pipe);
    }

    @Override
    public JoanaPipe updatePipe(JoanaUser currentUser, String pipeId, JoanaCreatePipeRequest createPipeRequest) {
	Optional<JoanaPipe> pipe = joanaPipeRepository.findById(pipeId);
	if (!pipe.isPresent()) {
	    throw new JoanaException("Pipe not found " + pipeId, HttpStatus.NOT_FOUND);
	}
	if (!pipe.get().getOwner().equals(currentUser.getEmail())) {
	    logger.error("Non owner update for pipe {}. User {}", pipe.get().getPipeName(), currentUser.getName());
	    throw new JoanaException("Invalid action", HttpStatus.FORBIDDEN);
	}
	pipe.get().setPipeName(createPipeRequest.getPipeName());
	Collections.sort(createPipeRequest.getPipeSteps());
	createPipeRequest.getPipeSteps().stream().forEach(step -> {
	    Optional<JoanaTask> task = joanaTaskRepository.findById(step.getStepId());
	    if (!task.isPresent()) {
		throw new JoanaException("Invalid step id in pipe request " + step.getStepId(), HttpStatus.BAD_REQUEST);
	    }
	    pipe.get().addPipeStep(task.get().getId(), task.get().getName());
	});
	return joanaPipeRepository.save(pipe.get());
    }

}

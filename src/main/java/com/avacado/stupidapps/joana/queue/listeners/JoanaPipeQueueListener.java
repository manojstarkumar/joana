package com.avacado.stupidapps.joana.queue.listeners;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.avacado.stupidapps.joana.domain.JoanaExecutionHistory;
import com.avacado.stupidapps.joana.domain.JoanaExecutionMode;
import com.avacado.stupidapps.joana.domain.JoanaStates;
import com.avacado.stupidapps.joana.domain.JoanaUser;
import com.avacado.stupidapps.joana.domain.pipe.JoanaPipe;
import com.avacado.stupidapps.joana.domain.pipe.JoanaPipeBase.JoanaPipeStep;
import com.avacado.stupidapps.joana.domain.pipe.JoanaPipeExecution;
import com.avacado.stupidapps.joana.domain.task.JoanaTaskExecution;
import com.avacado.stupidapps.joana.repository.JoanaPipeExecutionRepository;
import com.avacado.stupidapps.joana.repository.JoanaPipeRepository;
import com.avacado.stupidapps.joana.service.interfaces.JoanaTaskService;

@Component
@RabbitListener(queues = "${joana.pipe.rabbit.queue}")
public class JoanaPipeQueueListener {

    @Autowired
    private JoanaPipeRepository joanaPipeRepository;

    @Autowired
    private JoanaTaskService joanaTaskService;

    @Autowired
    private JoanaPipeExecutionRepository joanaPipeExecutionRepository;

    private Logger logger = LoggerFactory.getLogger(JoanaPipeQueueListener.class);

    @Transactional
    @RabbitHandler
    public void processPipeExecution(@Payload JoanaPipeExecution joanaPipeExecution) {
	JoanaPipe parentPipe = joanaPipeRepository.findById(joanaPipeExecution.getParentPipe()).get();

	List<JoanaPipeStep> pipeSteps = parentPipe.getPipeSteps();
	JoanaUser queuePipeUser = new JoanaUser();
	queuePipeUser.setName("Pipe - " + joanaPipeExecution.getPipeName());
	queuePipeUser.setEmail(joanaPipeExecution.getId());
	Collections.sort(pipeSteps);
	pipeSteps.forEach(step -> {
	    JoanaTaskExecution taskExecution = joanaTaskService.createTaskExecution(queuePipeUser, step.getTaskId(),
		    JoanaStates.WAITFORUPSTREAM, JoanaExecutionMode.PIPE);
	    joanaPipeExecution.addPipeStep(taskExecution.getId(), taskExecution.getName(), JoanaStates.WAITFORUPSTREAM);
	});

	joanaPipeExecution.setStartedAt(new Date());
	joanaPipeExecution.setState(JoanaStates.PROGRESS);
	joanaPipeExecutionRepository.save(joanaPipeExecution);

	if (parentPipe.getHistory() != null) {
	    if (parentPipe.getHistory().size() > 10) {
		logger.debug("Delete oldest execution from task {} {}", parentPipe.getId(), parentPipe.getPipeName());
		Collections.sort(parentPipe.getHistory());
		parentPipe.getHistory().remove(0);
	    }
	}
	parentPipe.addExecutionHistory(JoanaExecutionHistory.build().withExecutionId(joanaPipeExecution.getId())
		.withExecutionStatus(JoanaStates.PROGRESS).withExecutionDate(new Date()));
	joanaPipeRepository.save(parentPipe);

	joanaTaskService.startTaskExecution(joanaPipeExecution.getPipeSteps().stream()
		.filter(step -> step.getPosition() == 1).findFirst().get().getTaskId());
	joanaPipeExecutionRepository.save(joanaPipeExecution);
    }
}

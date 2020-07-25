package com.avacado.stupidapps.joana.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.avacado.stupidapps.joana.domain.pipe.JoanaPipeExecution;
import com.avacado.stupidapps.joana.domain.task.JoanaTaskExecution;
import com.avacado.stupidapps.joana.service.interfaces.JoanaQueueService;
import com.avacado.stupidapps.joana.utils.JoanaConstants;
import com.avacado.stupidapps.joana.utils.RequestUtils;

@Service
public class JoanaQueueServiceImpl implements JoanaQueueService {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Value("${joana.task.rabbit.exchange}")
    private String rabbitTaskExchange;
    @Value("${joana.task.rabbit.routingkey}")
    private String rabbitTaskRoutingKey;

    @Value("${joana.pipe.rabbit.exchange}")
    private String rabbitPipeExchange;
    @Value("${joana.pipe.rabbit.routingkey}")
    private String rabbitPipeRoutingKey;

    @Override
    public void queueTaskForProcessing(JoanaTaskExecution persistedExecution) {
	convertAndSend(persistedExecution);

    }

    @Override
    public void queuePipeForProcessing(JoanaPipeExecution persistedExecution) {
	convertAndSend(persistedExecution);
    }

    private void convertAndSend(Object persistedExecution) {
	rabbitTemplate.convertAndSend(rabbitTaskExchange, rabbitTaskRoutingKey, persistedExecution, postprocessor -> {
	    postprocessor.getMessageProperties().getHeaders().put(JoanaConstants.RMQ_DB_VALUE_HEADER,
		    RequestUtils.getDatabaseName());
	    return postprocessor;
	});
    }

}

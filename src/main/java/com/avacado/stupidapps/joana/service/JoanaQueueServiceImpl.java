package com.avacado.stupidapps.joana.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.avacado.stupidapps.joana.domain.pipe.JoanaPipeExecution;
import com.avacado.stupidapps.joana.domain.task.JoanaTaskExecution;
import com.avacado.stupidapps.joana.service.interfaces.JoanaQueueService;

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
	rabbitTemplate.convertAndSend(rabbitTaskExchange, rabbitTaskRoutingKey, persistedExecution);
	
    }

    @Override
    public void queuePipeForProcessing(JoanaPipeExecution persistedExecution) {
	rabbitTemplate.convertAndSend(rabbitPipeExchange, rabbitPipeRoutingKey, persistedExecution);
	
    }

}

package com.avacado.stupidapps.joana.configuration.queues;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JoanaRabbitMqConfiguration {

    @Value("${joana.task.rabbit.queue}")
    private String rabbitTaskQueue;
    @Value("${joana.task.rabbit.exchange}")
    private String rabbitTaskExchange;
    @Value("${joana.task.rabbit.routingkey}")
    private String rabbitTaskRoutingKey;

    @Value("${joana.pipe.rabbit.queue}")
    private String rabbitPipeQueue;
    @Value("${joana.pipe.rabbit.exchange}")
    private String rabbitPipeExchange;
    @Value("${joana.pipe.rabbit.routingkey}")
    private String rabbitPipeRoutingKey;

    @Value("${joana.dead.letter.rabbit.exchange}")
    private String deadLetterExchange;
    @Value("${joana.dead.letter.rabbit.routingkey}")
    private String deadLetterRoutingKey;

    @Bean
    public Declarables joanaBindings() {
	Queue taskQueue = QueueBuilder.durable(rabbitTaskQueue).deadLetterExchange(deadLetterExchange)
		.deadLetterRoutingKey(deadLetterRoutingKey).build();
	Queue pipeQueue = QueueBuilder.durable(rabbitPipeQueue).deadLetterExchange(deadLetterExchange)
		.deadLetterRoutingKey(deadLetterRoutingKey).build();

	TopicExchange taskExchange = new TopicExchange(rabbitTaskExchange);
	TopicExchange pipeExchange = new TopicExchange(rabbitPipeExchange);

	return new Declarables(taskQueue, taskExchange,
		BindingBuilder.bind(taskQueue).to(taskExchange).with(rabbitTaskRoutingKey), pipeQueue, pipeExchange,
		BindingBuilder.bind(pipeQueue).to(pipeExchange).with(rabbitPipeRoutingKey));
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
	return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
	final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
	rabbitTemplate.setMessageConverter(jsonMessageConverter());
	return rabbitTemplate;
    }
}

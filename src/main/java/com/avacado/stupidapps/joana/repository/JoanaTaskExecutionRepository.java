package com.avacado.stupidapps.joana.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.avacado.stupidapps.joana.domain.task.JoanaTaskExecution;

public interface JoanaTaskExecutionRepository extends MongoRepository<JoanaTaskExecution, String> {

    
}

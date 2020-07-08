package com.avacado.stupidapps.joana.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.avacado.stupidapps.joana.domain.task.JoanaTask;

public interface JoanaTaskRepository extends MongoRepository<JoanaTask, String>{

}

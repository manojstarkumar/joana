package com.avacado.stupidapps.joana.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.avacado.stupidapps.joana.domain.pipe.JoanaPipeExecution;

public interface JoanaPipeExecutionRepository extends MongoRepository<JoanaPipeExecution, String>{

}

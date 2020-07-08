package com.avacado.stupidapps.joana.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.avacado.stupidapps.joana.domain.pipe.JoanaPipe;

public interface JoanaPipeRepository extends MongoRepository<JoanaPipe, String>{

}

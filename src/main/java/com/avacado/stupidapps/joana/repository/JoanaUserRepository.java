package com.avacado.stupidapps.joana.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.avacado.stupidapps.joana.domain.JoanaUser;

@Repository
public interface JoanaUserRepository extends MongoRepository<JoanaUser, String>
{

  JoanaUser findByEmail(String email);
  JoanaUser findByxToken(String xToken);
}

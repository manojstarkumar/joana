package com.avacado.stupidapps.joana.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.avacado.stupidapps.joana.domain.JoanaOrganization;

@Repository
public interface JoanaOrganizationRepository extends MongoRepository<JoanaOrganization, String> {

    JoanaOrganization findByOrganization(String organization);
}

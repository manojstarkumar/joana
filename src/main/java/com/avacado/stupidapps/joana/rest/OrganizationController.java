package com.avacado.stupidapps.joana.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.avacado.stupidapps.joana.annotations.JoanaRestController;
import com.avacado.stupidapps.joana.domain.JoanaOrganization;
import com.avacado.stupidapps.joana.exceptions.JoanaException;
import com.avacado.stupidapps.joana.repository.JoanaOrganizationRepository;

@JoanaRestController
public class OrganizationController {

    @Autowired
    private JoanaOrganizationRepository joanaOrganizationRepository;
    
    @GetMapping("/{organization}/getxorg")
    public ResponseEntity<JoanaOrganization> getXOrg(@PathVariable String organization) {
	JoanaOrganization org = joanaOrganizationRepository.findByOrganization(organization);
	if(org != null) {
	    return new ResponseEntity<JoanaOrganization>(org, HttpStatus.OK);
	}
	throw new JoanaException("Invalid Organization code", HttpStatus.NOT_FOUND);
    }
}

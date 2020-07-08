package com.avacado.stupidapps.joana.rest;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.avacado.stupidapps.joana.annotations.CurrentLoggedInUser;
import com.avacado.stupidapps.joana.annotations.JoanaRestController;
import com.avacado.stupidapps.joana.domain.JoanaUser;
import com.avacado.stupidapps.joana.domain.pipe.JoanaPipe;
import com.avacado.stupidapps.joana.domain.pipe.JoanaPipeExecution;
import com.avacado.stupidapps.joana.protocol.request.JoanaCreatePipeRequest;
import com.avacado.stupidapps.joana.service.interfaces.JoanaPipeService;

@JoanaRestController
@RequestMapping("/pipe")
public class PipeController {

    @Autowired
    private JoanaPipeService joanaPipeService;

    @GetMapping("/{pipeId}/info")
    public JoanaPipe getPipe(@PathVariable String pipeId) {
	return joanaPipeService.getPipe(pipeId);
    }

    @PostMapping("/create")
    public JoanaPipe createPipe(@CurrentLoggedInUser JoanaUser currentUser,
	    @Valid @RequestBody JoanaCreatePipeRequest createPipeRequest) {
	return joanaPipeService.createPipe(currentUser, createPipeRequest);
    }

    @PutMapping("/{pipeId}/update")
    public JoanaPipe updatePipe(@CurrentLoggedInUser JoanaUser currentUser, @PathVariable String pipeId,
	    @Valid @RequestBody JoanaCreatePipeRequest createPipeRequest) {
	return joanaPipeService.updatePipe(currentUser, pipeId, createPipeRequest);
    }

    @DeleteMapping("/{pipeId}/delete")
    public ResponseEntity<Map<String, String>> deleteTask(@CurrentLoggedInUser JoanaUser user,
	    @PathVariable String pipeId) {
	joanaPipeService.deletePipe(user, pipeId);
	Map<String, String> responseMap = new HashMap<>();
	responseMap.put("status", "0");
	return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @GetMapping("/{pipeId}/start")
    public JoanaPipeExecution startPipe(@CurrentLoggedInUser JoanaUser currentUser, @PathVariable String pipeId) {
	return joanaPipeService.startPipeExecution(currentUser, pipeId);
    }
}

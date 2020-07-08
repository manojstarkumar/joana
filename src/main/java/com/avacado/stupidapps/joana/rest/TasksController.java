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
import com.avacado.stupidapps.joana.domain.JoanaExecutionMode;
import com.avacado.stupidapps.joana.domain.JoanaStates;
import com.avacado.stupidapps.joana.domain.JoanaUser;
import com.avacado.stupidapps.joana.domain.task.JoanaTask;
import com.avacado.stupidapps.joana.domain.task.JoanaTaskExecution;
import com.avacado.stupidapps.joana.protocol.request.JoanaCreateTaskRequest;
import com.avacado.stupidapps.joana.service.interfaces.JoanaTaskService;

@JoanaRestController
@RequestMapping("/task")
public class TasksController {

    @Autowired
    private JoanaTaskService joanaTaskService;

    @GetMapping("/{taskId}/start")
    public JoanaTaskExecution startTask(@CurrentLoggedInUser JoanaUser user, @PathVariable String taskId) {
	return joanaTaskService.startTaskExecution(joanaTaskService
		.createTaskExecution(user, taskId, JoanaStates.SCHEDULED, JoanaExecutionMode.STANDALONE).getId());
    }

    @PostMapping("/create")
    public JoanaTask createTask(@CurrentLoggedInUser JoanaUser user,
	    @Valid @RequestBody JoanaCreateTaskRequest createTaskRequest) {
	return joanaTaskService.createTask(user, createTaskRequest);
    }

    @PutMapping("/{taskId}/update")
    public JoanaTask updateTask(@CurrentLoggedInUser JoanaUser user, @PathVariable String taskId,
	    @Valid @RequestBody JoanaCreateTaskRequest createTaskRequest) {
	return joanaTaskService.updateTask(user, taskId, createTaskRequest);
    }

    @GetMapping("/{taskId}/info")
    public JoanaTask getTask(@PathVariable String taskId) {
	return joanaTaskService.getTask(taskId);
    }

    @DeleteMapping("/{taskId}/delete")
    public ResponseEntity<Map<String, String>> deleteTask(@CurrentLoggedInUser JoanaUser user,
	    @PathVariable String taskId) {
	joanaTaskService.deleteTask(user, taskId);
	Map<String, String> responseMap = new HashMap<>();
	responseMap.put("status", "0");
	return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }
}

package com.avacado.stupidapps.joana.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.avacado.stupidapps.joana.annotations.CurrentLoggedInUser;
import com.avacado.stupidapps.joana.annotations.JoanaRestController;
import com.avacado.stupidapps.joana.domain.JoanaReviewStates;
import com.avacado.stupidapps.joana.domain.JoanaUser;
import com.avacado.stupidapps.joana.domain.task.JoanaTaskExecution;
import com.avacado.stupidapps.joana.protocol.request.JoanaTaskExecutionUpdateRequest;
import com.avacado.stupidapps.joana.service.interfaces.JoanaTaskService;

@JoanaRestController
@RequestMapping("/task/execution")
public class TaskExecutionController {

    @Autowired
    private JoanaTaskService joanaTaskService;

    @GetMapping("/{executionId}/info")
    public JoanaTaskExecution getTaskExecution(@PathVariable String executionId) {
	return joanaTaskService.getTaskExecution(executionId);
    }

    @PostMapping("/{executionId}/approve")
    public JoanaTaskExecution approveTaskExecution(@CurrentLoggedInUser JoanaUser currentUser,
	    @PathVariable String executionId,
	    @RequestBody(required = false) JoanaTaskExecutionUpdateRequest joanaTaskExecutionUpdateRequest) {
	return joanaTaskService.updateExecutionTaskStatus(currentUser, executionId, JoanaReviewStates.APPROVED,
		joanaTaskExecutionUpdateRequest);
    }

    @PostMapping("/{executionId}/reject")
    public JoanaTaskExecution rejectTaskExecution(@CurrentLoggedInUser JoanaUser currentUser,
	    @PathVariable String executionId,
	    @RequestBody(required = false) JoanaTaskExecutionUpdateRequest joanaTaskExecutionUpdateRequest) {
	return joanaTaskService.updateExecutionTaskStatus(currentUser, executionId, JoanaReviewStates.REJECTED,
		joanaTaskExecutionUpdateRequest);
    }

    @PostMapping("/{executionId}/warnapprove")
    public JoanaTaskExecution warnApproveTaskExecution(@CurrentLoggedInUser JoanaUser currentUser,
	    @PathVariable String executionId,
	    @RequestBody(required = false) JoanaTaskExecutionUpdateRequest joanaTaskExecutionUpdateRequest) {
	return joanaTaskService.updateExecutionTaskStatus(currentUser, executionId, JoanaReviewStates.WARNING,
		joanaTaskExecutionUpdateRequest);
    }

    @PostMapping("/{executionId}/save")
    public JoanaTaskExecution saveTaskExecution(@CurrentLoggedInUser JoanaUser currentUser,
	    @PathVariable String executionId,
	    @RequestBody JoanaTaskExecutionUpdateRequest joanaTaskExecutionUpdateRequest) {
	return joanaTaskService.updateExecutionTaskStatus(currentUser, executionId, JoanaReviewStates.NONE,
		joanaTaskExecutionUpdateRequest);
    }

}

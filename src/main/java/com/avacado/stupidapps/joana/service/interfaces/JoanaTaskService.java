package com.avacado.stupidapps.joana.service.interfaces;

import com.avacado.stupidapps.joana.domain.JoanaExecutionMode;
import com.avacado.stupidapps.joana.domain.JoanaReviewStates;
import com.avacado.stupidapps.joana.domain.JoanaStates;
import com.avacado.stupidapps.joana.domain.JoanaUser;
import com.avacado.stupidapps.joana.domain.task.JoanaTask;
import com.avacado.stupidapps.joana.domain.task.JoanaTaskExecution;
import com.avacado.stupidapps.joana.protocol.request.JoanaCreateTaskRequest;
import com.avacado.stupidapps.joana.protocol.request.JoanaTaskExecutionUpdateRequest;

public interface JoanaTaskService {

    JoanaTask createTask(JoanaUser currentUser, JoanaCreateTaskRequest createTaskRequest);
    
    JoanaTask getTask(String taskId);
    
    void deleteTask(JoanaUser currentUser, String taskId);
    
    JoanaTask updateTask(JoanaUser currentUser, String taskId, JoanaCreateTaskRequest createTaskRequest);

    JoanaTaskExecution startTaskExecution(String taskId);

    JoanaTaskExecution createTaskExecution(JoanaUser currentUser, String taskId, JoanaStates taskState,
	    JoanaExecutionMode executionMode);

    JoanaTaskExecution getTaskExecution(String taskId);

    JoanaTaskExecution updateExecutionTaskStatus(JoanaUser currentUser, String taskId, JoanaReviewStates state,
	    JoanaTaskExecutionUpdateRequest joanaTaskExecutionUpdateRequest);

}

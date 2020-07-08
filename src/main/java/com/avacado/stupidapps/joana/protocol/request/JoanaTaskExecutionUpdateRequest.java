package com.avacado.stupidapps.joana.protocol.request;

import java.util.List;

import com.avacado.stupidapps.joana.domain.JoanaReviewStates;

public class JoanaTaskExecutionUpdateRequest {

    private List<UpdateRequestTaskChange> updateRequestTaskChanges;
    private List<String> comments;

    public List<UpdateRequestTaskChange> getUpdateRequestTaskChanges() {
	return updateRequestTaskChanges;
    }

    public void setUpdateRequestTaskChanges(List<UpdateRequestTaskChange> updateRequestTaskChanges) {
	this.updateRequestTaskChanges = updateRequestTaskChanges;
    }

    public List<String> getComments() {
	return comments;
    }

    public void setComments(List<String> comments) {
	this.comments = comments;
    }

    public static class UpdateRequestTaskChange {
	private int position;
	private String taskName;
	private JoanaReviewStates state;

	public int getPosition() {
	    return position;
	}

	public void setPosition(int position) {
	    this.position = position;
	}

	public String getTaskName() {
	    return taskName;
	}

	public void setTaskName(String taskName) {
	    this.taskName = taskName;
	}

	public JoanaReviewStates getState() {
	    return state;
	}

	public void setState(JoanaReviewStates state) {
	    this.state = state;
	}

    }
}

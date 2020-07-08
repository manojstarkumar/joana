package com.avacado.stupidapps.joana.protocol.request;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class JoanaCreateTaskRequest {

    @NotBlank
    String name;
    
    @NotNull
    @Valid
    List<JoanaCreateTaskRequestProtocolActions> actions;
    
    @NotNull
    List<String> reviewers;


    public List<String> getReviewers() {
	return reviewers;
    }

    public void setReviewers(List<String> reviewers) {
	this.reviewers = reviewers;
    }

    public static class JoanaCreateTaskRequestProtocolActions {
	
	@NotNull
	int position;
	
	@NotBlank
	String name;

	public int getPosition() {
	    return position;
	}

	public void setPosition(int position) {
	    this.position = position;
	}

	public String getName() {
	    return name;
	}

	public void setName(String name) {
	    this.name = name;
	}

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<JoanaCreateTaskRequestProtocolActions> getActions() {
        return actions;
    }

    public void setActions(List<JoanaCreateTaskRequestProtocolActions> actions) {
        this.actions = actions;
    }
}

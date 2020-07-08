package com.avacado.stupidapps.joana.domain.task;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;

import com.avacado.stupidapps.joana.domain.JoanaReviewStates;
import com.avacado.stupidapps.joana.domain.JoanaUser;

@Valid
public class JoanaTaskBase {

    @Id
    private String id;

    @NotBlank
    private String name;

    @Valid
    private List<JoanaTaskAction> actions;

    @Valid
    private List<JoanaTaskReviewer> reviewers;

    public String getId() {
	return id;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public List<JoanaTaskAction> getActions() {
	return actions;
    }

    public void addAction(JoanaTaskAction action) {
	if (this.actions == null)
	    this.actions = new ArrayList<>();
	if(action.getPosition() == 0)
	    action.setPosition(this.actions.size() + 1);
	if (action.getState() == null)
	    action.setState(JoanaReviewStates.NONE);
	this.actions.add(action);
    }

    public List<JoanaTaskReviewer> getReviewers() {
	return reviewers;
    }

    public void addReviewer(JoanaUser joanaUser) {
	if (this.reviewers == null)
	    this.reviewers = new ArrayList<>();
	JoanaTaskReviewer taskReviewer = new JoanaTaskReviewer();
	taskReviewer.setEmail(joanaUser.getEmail());
	taskReviewer.setName(joanaUser.getName());
	taskReviewer.setReviewState(JoanaReviewStates.NONE);
	this.reviewers.add(taskReviewer);
    }

    public static class JoanaTaskAction {
	private int position;

	@NotBlank
	private String name;

	private JoanaReviewStates state;

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

	public JoanaReviewStates getState() {
	    return state;
	}

	public void setState(JoanaReviewStates state) {
	    this.state = state;
	}
    }

    public static class JoanaTaskReviewer {

	@NotBlank
	private String name;

	@NotBlank
	private String email;

	private JoanaReviewStates reviewState;

	public String getName() {
	    return name;
	}

	public void setName(String name) {
	    this.name = name;
	}

	public String getEmail() {
	    return email;
	}

	public void setEmail(String email) {
	    this.email = email;
	}

	public JoanaReviewStates getReviewState() {
	    return reviewState;
	}

	public void setReviewState(JoanaReviewStates reviewState) {
	    this.reviewState = reviewState;
	}
    }
}

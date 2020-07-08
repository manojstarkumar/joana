package com.avacado.stupidapps.joana.protocol.request;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class JoanaCreatePipeRequest {

    @NotBlank
    private String pipeName;

    @Valid
    @NotNull
    private List<JoanaCreatePipeRequestPipeSteps> pipeSteps;

    public String getPipeName() {
	return pipeName;
    }

    public void setPipeName(String pipeName) {
	this.pipeName = pipeName;
    }

    public List<JoanaCreatePipeRequestPipeSteps> getPipeSteps() {
	return pipeSteps;
    }

    public void setPipeSteps(List<JoanaCreatePipeRequestPipeSteps> pipeSteps) {
	this.pipeSteps = pipeSteps;
    }

    public static class JoanaCreatePipeRequestPipeSteps implements Comparable<JoanaCreatePipeRequestPipeSteps>{
	
	@NotNull
	private int position;
	
	@NotBlank
	private String stepId;

	public int getPosition() {
	    return position;
	}

	public void setPosition(int position) {
	    this.position = position;
	}

	public String getStepId() {
	    return stepId;
	}

	public void setStepId(String stepId) {
	    this.stepId = stepId;
	}

	@Override
	public int compareTo(JoanaCreatePipeRequestPipeSteps step) {
	   return this.position - step.position;
	}
    }

}

package com.avacado.stupidapps.joana.domain.pipe;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;

import com.avacado.stupidapps.joana.domain.JoanaStates;

@Valid
public class JoanaPipeBase {
    @Id
    private String id;

    @NotNull
    private String pipeName;

    private List<String> pipeWatchers;

    public String getId() {
	return id;
    }

    public String getPipeName() {
	return pipeName;
    }

    public void setPipeName(String pipeName) {
	this.pipeName = pipeName;
    }

    @Valid
    private List<JoanaPipeStep> pipeSteps;

    public List<JoanaPipeStep> getPipeSteps() {
	return pipeSteps;
    }

    public void addPipeStep(String taskId, String stepName) {
	if (this.pipeSteps == null)
	    this.pipeSteps = new ArrayList<>();
	JoanaPipeStep step = new JoanaPipeStep();
	step.setTaskId(taskId);
	step.setStepName(stepName);
	step.setPosition(this.pipeSteps.size() + 1);
	step.setStepState(JoanaStates.NONE);
	this.pipeSteps.add(step);
    }

    public void addPipeStep(String taskId, String stepName, JoanaStates state) {
	addPipeStep(taskId, stepName);
	this.getPipeSteps().get(this.pipeSteps.size() - 1).setStepState(state);
    }

    public List<String> getPipeWatchers() {
	return pipeWatchers;
    }

    public void addPipeWatchers(String pipeWatcher) {
	if (this.pipeWatchers == null)
	    this.pipeWatchers = new ArrayList<>();
	this.pipeWatchers.add(pipeWatcher);
    }

    public static class JoanaPipeStep implements Comparable<JoanaPipeStep> {
	private int position;

	@NotBlank
	private String taskId;

	@NotBlank
	private String stepName;

	private JoanaStates stepState;

	public int getPosition() {
	    return position;
	}

	public void setPosition(int position) {
	    this.position = position;
	}

	public String getTaskId() {
	    return taskId;
	}

	public void setTaskId(String taskId) {
	    this.taskId = taskId;
	}

	@Override
	public int compareTo(JoanaPipeStep pipeStep) {
	    return this.position - pipeStep.position;
	}

	public JoanaStates getStepState() {
	    return stepState;
	}

	public void setStepState(JoanaStates stepState) {
	    this.stepState = stepState;
	}

	public String getStepName() {
	    return stepName;
	}

	public void setStepName(String stepName) {
	    this.stepName = stepName;
	}

    }

}

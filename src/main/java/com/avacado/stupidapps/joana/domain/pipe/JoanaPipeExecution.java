package com.avacado.stupidapps.joana.domain.pipe;


import java.util.Date;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.mongodb.core.mapping.Document;

import com.avacado.stupidapps.joana.domain.JoanaStates;

@Document("joana_pipes_execution")
@Valid
public class JoanaPipeExecution extends JoanaPipeBase {

    @NotBlank
    private String triggeredBy;

    @NotBlank
    private String parentPipe;
    
    private int currentStep;

    @NotNull
    private JoanaStates state;

    @NotNull
    private Date startedAt;

    private Date completedAt;

    public void setStartedAt(Date start) {
	if(start != null)
	    this.startedAt = (Date) start.clone();
    }

    public Date getStartedAt() {
	if(this.startedAt != null)
	    return (Date) this.startedAt.clone();
	return null;
    }

    public void setCompletedAt(Date finish) {
	if(finish != null)
	    this.completedAt = (Date) finish.clone();
    }

    public Date getCompletedAt() {
	if(this.completedAt != null)
	    return (Date) this.completedAt.clone();
	return null;
    }

    public JoanaStates getState() {
	return state;
    }

    public void setState(JoanaStates state) {
	this.state = state;
    }

    public String getTriggeredBy() {
	return triggeredBy;
    }

    public void setTriggeredBy(String triggeredBy) {
	this.triggeredBy = triggeredBy;
    }

    public String getParentPipe() {
	return parentPipe;
    }

    public void setParentPipe(String parentPipe) {
	this.parentPipe = parentPipe;
    }

    public int getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(int currentStep) {
        this.currentStep = currentStep;
    }

}

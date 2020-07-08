package com.avacado.stupidapps.joana.domain;

import java.util.Date;

import javax.validation.constraints.NotNull;

public class JoanaExecutionHistory implements Comparable<JoanaExecutionHistory> {
    @NotNull
    private String executionId;
    @NotNull
    private JoanaStates executionStatus;
    @NotNull
    private Date executionDate;

    public String getExecutionId() {
	return executionId;
    }

    public void setExecutionId(String executionId) {
	this.executionId = executionId;
    }

    public JoanaStates getExecutionStatus() {
	return executionStatus;
    }

    public void setExecutionStatus(JoanaStates executionStatus) {
	this.executionStatus = executionStatus;
    }

    public Date getExecutionDate() {
	if (executionDate != null)
	    return (Date) executionDate.clone();
	return null;
    }

    public void setExecutionDate(Date executionDate) {
	if (executionDate != null)
	    this.executionDate = (Date) executionDate.clone();
    }

    public static JoanaExecutionHistory build() {
	return new JoanaExecutionHistory();
    }

    public JoanaExecutionHistory withExecutionId(String executionId) {
	this.setExecutionId(executionId);
	return this;
    }

    public JoanaExecutionHistory withExecutionStatus(JoanaStates executionStatus) {
	this.setExecutionStatus(executionStatus);
	return this;
    }

    public JoanaExecutionHistory withExecutionDate(Date executionDate) {
	this.setExecutionDate(executionDate);
	return this;
    }

    @Override
    public int compareTo(JoanaExecutionHistory joanaExecutionHistory) {
	if (this.executionDate.before(joanaExecutionHistory.getExecutionDate()))
	    return -1;
	if (this.executionDate.after(joanaExecutionHistory.getExecutionDate()))
	    return 1;
	return 0;
    }
}

package com.avacado.stupidapps.joana.domain;

public enum JoanaExecutionMode {

    STANDALONE("Standalone"),
    PIPE("Pipe");
    
    private String executionMode;
    
    private JoanaExecutionMode(String executionMode) {
	this.executionMode = executionMode;
    }
    
    public String getExecutionMode() {
	return this.executionMode;
    }
}

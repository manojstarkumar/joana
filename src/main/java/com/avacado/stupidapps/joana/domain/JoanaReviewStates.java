package com.avacado.stupidapps.joana.domain;

public enum JoanaReviewStates {

    NONE("None"),
    APPROVED("Approved"),
    REJECTED("Rejected"),
    WARNING("Approved with Warning");
    
    private String joanaReviewDisplayState;
    
    private JoanaReviewStates(String joanaReviewDisplayState) {
	this.joanaReviewDisplayState = joanaReviewDisplayState;
    }
    
    public String getJoanaReviewDisplayState() {
	return this.joanaReviewDisplayState;
    }
}

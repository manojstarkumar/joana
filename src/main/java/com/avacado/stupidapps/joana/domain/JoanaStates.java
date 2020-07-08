package com.avacado.stupidapps.joana.domain;

public enum JoanaStates
{

  NONE("None"),
  SCHEDULED("Scheduled"),
  WAITFORUPSTREAM("Waiting for upstream"),
  PROGRESS("In Progress"),
  COMPLETE("Completed"),
  FAILED("Failed"),
  ABORT("Aborted"), 
  UPSTREAMFAIL("Upstream task failed");

  private String displayName;
  
  private JoanaStates(String displayName)
  {
    this.displayName = displayName;
  }
  
  public String getDisplayName() {
    return this.displayName;
  }
}

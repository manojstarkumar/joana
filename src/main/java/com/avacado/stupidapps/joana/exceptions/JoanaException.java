package com.avacado.stupidapps.joana.exceptions;

import org.springframework.http.HttpStatus;

public class JoanaException extends RuntimeException
{
  private static final long serialVersionUID = -7185549301786673015L;
  private String exceptionMessage;
  private HttpStatus httpStatus;
  
  public JoanaException(String exceptionMessage, HttpStatus httpStatus) {
    this.exceptionMessage = exceptionMessage;
    this.httpStatus = httpStatus;
  }
  
  public JoanaException(String exceptionMessage, HttpStatus httpStatus, StackTraceElement[] stackTraceElement) {
    this.exceptionMessage = exceptionMessage;
    this.httpStatus = httpStatus;
    this.setStackTrace(stackTraceElement);
  }

  public String getExceptionMessage()
  {
    return exceptionMessage;
  }

  public HttpStatus getHttpStatus()
  {
    return httpStatus;
  }

}

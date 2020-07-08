package com.avacado.stupidapps.joana.configuration.method.security;

import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

public class JoanaMethodSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

  private JoanaMethodSecurityService joanaMethodSecurityService;

  public JoanaMethodSecurityExpressionRoot(Authentication authentication, JoanaMethodSecurityService joanaMethodSecurityService)
  {
    super(authentication);
    this.joanaMethodSecurityService = joanaMethodSecurityService;
    this.joanaMethodSecurityService.setCurrentUser(authentication);
  }

  public boolean isAdmin() {
    return this.joanaMethodSecurityService.isAdmin();
  }

  public boolean canModifyTask(Long taskId) {
    return this.joanaMethodSecurityService.canModifyTask(taskId);
  }

  private Object filterObject;
  private Object returnObject;
  private Object target;

  @Override
  public void setFilterObject(Object filterObject) {
    this.filterObject = filterObject;
  }

  @Override
  public Object getFilterObject() {
    return filterObject;
  }

  @Override
  public void setReturnObject(Object returnObject) {
    this.returnObject = returnObject;
  }

  @Override
  public Object getReturnObject() {
    return returnObject;
  }
  
  void setThis(Object target) {
    this.target = target;
  }

  @Override
  public Object getThis() {
    return target;
  }
}
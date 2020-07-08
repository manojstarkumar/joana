package com.avacado.stupidapps.joana.configuration.method.security;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class JoanaPrePostSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler
{
  private AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();
  
  @Autowired
  private JoanaMethodSecurityService joanaMethodSecurityService;

  @Override
  protected MethodSecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication,
      MethodInvocation invocation)
  {
    JoanaMethodSecurityExpressionRoot root = new JoanaMethodSecurityExpressionRoot(authentication, joanaMethodSecurityService);
    root.setPermissionEvaluator(getPermissionEvaluator());
    root.setThis(invocation.getThis());
    root.setTrustResolver(this.trustResolver);
    root.setRoleHierarchy(getRoleHierarchy());
    return root;
  }
}
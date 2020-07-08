package com.avacado.stupidapps.joana.configuration.method.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class JoanaPrePostSecurityConfiguration extends GlobalMethodSecurityConfiguration
{
  @Autowired
  private JoanaPrePostSecurityExpressionHandler joanaPrePostSecurityExpressionHandler;
  
  @Override
  protected MethodSecurityExpressionHandler createExpressionHandler()
  {
    return joanaPrePostSecurityExpressionHandler;
  }
}
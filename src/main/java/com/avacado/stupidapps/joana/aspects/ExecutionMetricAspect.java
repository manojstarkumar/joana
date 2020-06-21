package com.avacado.stupidapps.joana.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.avacado.stupidapps.joana.annotations.ProcessMethodMetrics;

@Aspect
@Component
public class ExecutionMetricAspect
{

  private static final Logger logger = LoggerFactory.getLogger(ExecutionMetricAspect.class);
  
  @Before("@annotation(processMethodMetrics)")
  public void processExecutionMetrics(JoinPoint joinPoint, ProcessMethodMetrics processMethodMetrics) 
  {
    logger.debug("Proessing method {} with api value {}", joinPoint.getSignature().toShortString(), processMethodMetrics.value());
  }
  
}

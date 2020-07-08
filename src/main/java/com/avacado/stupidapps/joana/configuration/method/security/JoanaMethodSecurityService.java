package com.avacado.stupidapps.joana.configuration.method.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.avacado.stupidapps.joana.domain.JoanaUser;

@Service
public class JoanaMethodSecurityService
{
  
  JoanaUser currentUser;
  
  public boolean isAdmin()
  {
    return currentUser.getAuthorities().parallelStream().anyMatch(auth -> auth.getAuthority().equalsIgnoreCase("ROLE_ADMIN"));
  }

  public boolean canModifyTask(Long taskId)
  {
    return false;
  }
  
  public void setCurrentUser(Authentication authentication) {
    this.currentUser = (JoanaUser) authentication.getPrincipal();
  }

}

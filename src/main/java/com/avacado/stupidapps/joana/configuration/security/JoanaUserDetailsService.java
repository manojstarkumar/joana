package com.avacado.stupidapps.joana.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.avacado.stupidapps.joana.domain.JoanaUser;
import com.avacado.stupidapps.joana.repository.JoanaUserRepository;

@Service
public class JoanaUserDetailsService implements UserDetailsService
{

  @Autowired
  JoanaUserRepository userRespository;
  
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
  {
    JoanaUser user = userRespository.findByEmail(username);
    if(user == null)
      throw new UsernameNotFoundException("Invalid username/password");
    Authentication authResult = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(authResult);
    return new User(user.getEmail(), user.getPassword(), user.getAuthorities());
  }

}

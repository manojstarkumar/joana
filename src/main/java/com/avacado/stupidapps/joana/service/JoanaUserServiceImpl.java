package com.avacado.stupidapps.joana.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.avacado.stupidapps.joana.domain.JoanaUser;
import com.avacado.stupidapps.joana.repository.JoanaUserRepository;
import com.avacado.stupidapps.joana.service.interfaces.JoanaUserService;
import com.avacado.stupidapps.joana.utils.JoanaUtils;

@Service
public class JoanaUserServiceImpl implements JoanaUserService
{

  private static Logger logger = LoggerFactory.getLogger(JoanaUserServiceImpl.class);
  
  @Autowired
  JoanaUserRepository joanaUserRepository;

  @Override
  public JoanaUser getUserByEmail(String email)
  {
    return joanaUserRepository.findByEmail(email);
  }
  
  @Override
  public JoanaUser createUser(String email, String password)
  {
    JoanaUser joanaUser = new JoanaUser();
    joanaUser.setEmail(email);
    joanaUser.setPassword(new BCryptPasswordEncoder().encode(password));
    joanaUser.setAuthoritiesString("ROLE_BASIC");
    joanaUser.setxToken(JoanaUtils.generateSecureUserToken(email));
    return joanaUserRepository.save(joanaUser);
  }

}

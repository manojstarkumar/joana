package com.avacado.stupidapps.joana.rest;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.avacado.stupidapps.joana.annotations.CurrentLoggedInUser;
import com.avacado.stupidapps.joana.annotations.JoanaRestController;
import com.avacado.stupidapps.joana.domain.JoanaUser;
import com.avacado.stupidapps.joana.protocol.request.CreateUserRequest;
import com.avacado.stupidapps.joana.protocol.response.JoanaUserProtocolResponse;
import com.avacado.stupidapps.joana.service.interfaces.JoanaUserService;

@JoanaRestController
public class UserController
{

  @Autowired
  ConversionService conversionService;
  
  @Autowired
  JoanaUserService joanaUserService;
  
  @PostMapping("/register")
  public JoanaUserProtocolResponse registerUser(@Valid CreateUserRequest createUserRequest) {
    JoanaUser user = joanaUserService.createUser(createUserRequest.getEmail(), createUserRequest.getPassword());
    return conversionService.convert(user, JoanaUserProtocolResponse.class);
  }
  
  @GetMapping("/login")
  public JoanaUserProtocolResponse getUser(@CurrentLoggedInUser JoanaUser user) {
    return conversionService.convert(user, JoanaUserProtocolResponse.class);
  }
  
  @GetMapping("/authStatus")
  public JoanaUserProtocolResponse getAuthStatus(@CurrentLoggedInUser JoanaUser user) {
    return conversionService.convert(user, JoanaUserProtocolResponse.class);
  }
}

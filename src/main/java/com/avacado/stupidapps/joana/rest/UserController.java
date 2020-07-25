package com.avacado.stupidapps.joana.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.avacado.stupidapps.joana.annotations.CurrentLoggedInUser;
import com.avacado.stupidapps.joana.annotations.JoanaRestController;
import com.avacado.stupidapps.joana.domain.JoanaUser;
import com.avacado.stupidapps.joana.domain.task.JoanaTaskExecution;
import com.avacado.stupidapps.joana.protocol.request.CreateUserRequest;
import com.avacado.stupidapps.joana.protocol.response.JoanaUserProtocolResponse;
import com.avacado.stupidapps.joana.service.interfaces.JoanaTaskService;
import com.avacado.stupidapps.joana.service.interfaces.JoanaUserService;

@JoanaRestController
@RequestMapping("/user")
public class UserController
{

  @Autowired
  private ConversionService conversionService;
  
  @Autowired
  private JoanaTaskService joanaTaskService;
  
  @Autowired
  private JoanaUserService joanaUserService;
  
  @PostMapping("/register")
  public JoanaUserProtocolResponse registerUser(@Valid @RequestBody CreateUserRequest createUserRequest) {
    JoanaUser user = joanaUserService.createUser(createUserRequest.getEmail(), createUserRequest.getName());
    return conversionService.convert(user, JoanaUserProtocolResponse.class);
  }
  
  @GetMapping("/login")
  public JoanaUserProtocolResponse getUser(@CurrentLoggedInUser JoanaUser user) {
    return conversionService.convert(user, JoanaUserProtocolResponse.class);
  }
  
  @GetMapping("/info")
  public JoanaUserProtocolResponse getAuthStatus(@CurrentLoggedInUser JoanaUser user) {
    return conversionService.convert(user, JoanaUserProtocolResponse.class);
  }
  
  @GetMapping("/pendingtasks")
  public ResponseEntity<Map<String, List<JoanaTaskExecution>>> getPendingTasks(@CurrentLoggedInUser JoanaUser user) {
      Map<String, List<JoanaTaskExecution>> pendingTasks = new HashMap<>();
      pendingTasks.put("pendingTasks", joanaTaskService.getPendingTasks(user));
      return new ResponseEntity<>(pendingTasks, HttpStatus.OK);
  }
}

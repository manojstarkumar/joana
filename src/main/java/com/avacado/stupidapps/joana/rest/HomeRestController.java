package com.avacado.stupidapps.joana.rest;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.avacado.stupidapps.joana.annotations.JoanaRestController;
import com.avacado.stupidapps.joana.annotations.ProcessMethodMetrics;
import com.avacado.stupidapps.joana.domain.JoanaUser;
import com.avacado.stupidapps.joana.protocol.response.JoanaUserProtocolResponse;
import com.avacado.stupidapps.joana.service.interfaces.JoanaUserService;

@JoanaRestController
@RequestMapping("/test")
public class HomeRestController
{

  @Autowired
  ConversionService conversionService;
  
  @Autowired
  JoanaUserService joanaUserService;
  
  @ProcessMethodMetrics("home")
  @GetMapping("/")
  public Map<String, String> home() throws JSONException {
    Map<String, String> json = new HashMap<String, String>();
    json.put("hello", "world");
    return json;
  }
  
  @ProcessMethodMetrics("user")
  @GetMapping("/user")
  public JoanaUserProtocolResponse getUser() throws JSONException {
    JoanaUser user = joanaUserService.getUserByEmail("manoj@manoj.com");
    return conversionService.convert(user, JoanaUserProtocolResponse.class);
  }

}

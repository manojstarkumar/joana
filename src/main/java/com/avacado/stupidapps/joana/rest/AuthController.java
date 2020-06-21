package com.avacado.stupidapps.joana.rest;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.springframework.web.bind.annotation.GetMapping;

import com.avacado.stupidapps.joana.annotations.JoanaRestController;
import com.avacado.stupidapps.joana.annotations.ProcessMethodMetrics;

@JoanaRestController
public class AuthController
{

  @ProcessMethodMetrics("auth")
  @GetMapping("/auth")
  public Map<String, String> home() throws JSONException {
    Map<String, String> json = new HashMap<String, String>();
    json.put("authentication", "world");
    return json;
  }
}

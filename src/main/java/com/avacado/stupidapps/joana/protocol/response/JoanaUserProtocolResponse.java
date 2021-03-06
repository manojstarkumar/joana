package com.avacado.stupidapps.joana.protocol.response;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JoanaUserProtocolResponse implements Serializable
{

  private static final long serialVersionUID = -6964116678033973382L;

  @JsonProperty
  private String email;
  
  @JsonProperty
  private String xToken;
  
  @JsonProperty
  private String name;
  
  @JsonProperty
  private List<String> authorities;

  public String getEmail()
  {
    return email;
  }

  public void setEmail(String email)
  {
    this.email = email;
  }

  public String getxToken()
  {
    return xToken;
  }

  public void setxToken(String xToken)
  {
    this.xToken = xToken;
  }

  public List<String> getAuthorities()
  {
    return authorities;
  }

  public void setAuthorities(List<String> authorities)
  {
    this.authorities = authorities;
  }

public String getName() {
    return name;
}

public void setName(String name) {
    this.name = name;
}
}

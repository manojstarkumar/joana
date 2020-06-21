package com.avacado.stupidapps.joana.protocol.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JoanaUserProtocolResponse
{
  @JsonProperty
  public String email;
  
  @JsonProperty
  public String xToken;
  
  @JsonProperty
  public String authorities;

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

  public String getAuthorities()
  {
    return authorities;
  }

  public void setAuthorities(String authorities)
  {
    this.authorities = authorities;
  }
}

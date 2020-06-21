package com.avacado.stupidapps.joana.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("joana_users")
public class JoanaUser
{
  @Id
  public String id;

  String email;
  String password;
  String authoritiesString;
  String xToken;
  
  public String getEmail()
  {
    return email;
  }
  public void setEmail(String email)
  {
    this.email = email;
  }
  public String getPassword()
  {
    return password;
  }
  public void setPassword(String password)
  {
    this.password = password;
  }
  public String getAuthoritiesString()
  {
    return authoritiesString;
  }
  public void setAuthoritiesString(String authoritiesString)
  {
    this.authoritiesString = authoritiesString;
  }
  public String getxToken()
  {
    return xToken;
  }
  public void setxToken(String xToken)
  {
    this.xToken = xToken;
  }

}

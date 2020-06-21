package com.avacado.stupidapps.joana.protocol.request;

import javax.validation.constraints.NotNull;

public class CreateUserRequest
{
  @NotNull
  public String email;
  @NotNull
  public String password;
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

}

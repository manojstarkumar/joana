package com.avacado.stupidapps.joana.configuration.security;

import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

public class JoanaAuthenticationToken extends PreAuthenticatedAuthenticationToken
{

  private static final long serialVersionUID = -5845686924344757084L;

  public JoanaAuthenticationToken(String token)
  {
    super(token, null);
  }

}

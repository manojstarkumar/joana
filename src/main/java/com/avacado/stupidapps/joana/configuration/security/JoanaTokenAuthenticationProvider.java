package com.avacado.stupidapps.joana.configuration.security;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.avacado.stupidapps.joana.domain.JoanaUser;
import com.avacado.stupidapps.joana.exceptions.JoanaException;
import com.avacado.stupidapps.joana.repository.JoanaUserRepository;

@Component
public class JoanaTokenAuthenticationProvider implements AuthenticationProvider
{

  @Autowired
  JoanaUserRepository joanaUserRepository;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException
  {
    String token = (String) authentication.getPrincipal();
    if (!StringUtils.isEmpty(token))
    {
      JoanaUser user = joanaUserRepository.findByxToken(token);
      if (user != null)
      {
        Authentication authResult = new UsernamePasswordAuthenticationToken(user, user.getPassword(),
            AuthorityUtils.commaSeparatedStringToAuthorityList(user.getAuthoritiesString()));
        SecurityContextHolder.getContext().setAuthentication(authResult);
        return authResult;
      }
    }
    throw new JoanaException("Token expired/invalid", HttpStatus.UNAUTHORIZED);
  }

  @Override
  public boolean supports(Class<?> authentication)
  {
    return authentication.equals(JoanaAuthenticationToken.class);
  }

}

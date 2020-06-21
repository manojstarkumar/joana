package com.avacado.stupidapps.joana.converters;

import org.springframework.core.convert.converter.Converter;

import com.avacado.stupidapps.joana.domain.JoanaUser;
import com.avacado.stupidapps.joana.protocol.response.JoanaUserProtocolResponse;

public class JoanaUsersToProtocolConverter implements Converter<JoanaUser, JoanaUserProtocolResponse>
{

  @Override
  public JoanaUserProtocolResponse convert(JoanaUser joanaUser)
  {
    JoanaUserProtocolResponse response = new JoanaUserProtocolResponse();
    response.setEmail(joanaUser.getEmail());
    response.setxToken(joanaUser.getxToken());
    response.setAuthorities(joanaUser.getAuthoritiesString());
    return response;
  }

}

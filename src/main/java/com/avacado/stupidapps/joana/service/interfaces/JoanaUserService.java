package com.avacado.stupidapps.joana.service.interfaces;

import com.avacado.stupidapps.joana.domain.JoanaUser;

public interface JoanaUserService {

    JoanaUser getUserByEmail(String email);

    JoanaUser createUser(String email, String name);

}

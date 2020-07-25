package com.avacado.stupidapps.joana.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.avacado.stupidapps.joana.domain.JoanaUser;
import com.avacado.stupidapps.joana.repository.JoanaUserRepository;
import com.avacado.stupidapps.joana.service.interfaces.JoanaUserService;
import com.avacado.stupidapps.joana.utils.JoanaConstants;
import com.avacado.stupidapps.joana.utils.JoanaUtils;

@Service
public class JoanaUserServiceImpl implements JoanaUserService {

    @Autowired
    JoanaUserRepository joanaUserRepository;

    @Override
    public JoanaUser getUserByEmail(String email) {
	return joanaUserRepository.findByEmail(email);
    }

    @PreAuthorize("isAdmin()")
    @Override
    public JoanaUser createUser(String email, String name) {
	String generatedPassword = JoanaUtils.generateSecureRandomString(8);
	JoanaUser joanaUser = new JoanaUser();
	joanaUser.setEmail(email);
	joanaUser.setName(name);
	joanaUser.setPassword(new BCryptPasswordEncoder().encode(generatedPassword));
	List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
	grantedAuthorities.add(new SimpleGrantedAuthority(JoanaConstants.DEFAULT_AUTHORITY));
	joanaUser.setAuthorities(grantedAuthorities);
	joanaUser.setxToken(JoanaUtils.generateSecureUserToken(email));
	return joanaUserRepository.save(joanaUser);
    }

}

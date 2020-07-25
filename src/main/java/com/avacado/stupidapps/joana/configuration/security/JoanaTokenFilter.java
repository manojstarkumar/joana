package com.avacado.stupidapps.joana.configuration.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.www.BasicAuthenticationConverter;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.avacado.stupidapps.joana.exceptions.JoanaException;
import com.avacado.stupidapps.joana.utils.RequestUtils;

public class JoanaTokenFilter extends OncePerRequestFilter {

    private List<String> openEndPoints;
    private List<String> passwordEndPoints;
    private AuthenticationManager authenticationManager;
    private AntPathMatcher matcher;

    public JoanaTokenFilter(AuthenticationManager authenticationManager, String[] openEndpoints,
	    String[] passwordEndPoints) {
	this.authenticationManager = authenticationManager;
	this.openEndPoints = Arrays.asList(openEndpoints);
	this.passwordEndPoints = Arrays.asList(passwordEndPoints);
	this.matcher = new AntPathMatcher();
    }


    private boolean isPasswordAuthEndPoint(HttpServletRequest request) {
	Optional<String> pathMatch = passwordEndPoints.parallelStream()
		.filter(endpoint -> matcher.match(endpoint, request.getServletPath())).findFirst();
	return pathMatch.isPresent();
    }

    private boolean isOpenEndPoint(HttpServletRequest request) {
	Optional<String> pathMatch = openEndPoints.parallelStream()
		.filter(endpoint -> matcher.match(endpoint, request.getServletPath())).findFirst();
	return pathMatch.isPresent();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	    throws ServletException, IOException {
	HttpServletRequest httpServletRequest = (HttpServletRequest) request;
	Optional<String> token = Optional.ofNullable(httpServletRequest.getHeader("X-Auth-Token"));
	
	if(httpServletRequest.getHeader("X-Org") != null) {
	    RequestUtils.setDatabaseName(httpServletRequest.getHeader("X-Org"));
	}
	
	// Process token from header and authenticate
	if (!isOpenEndPoint(httpServletRequest)) {
	    if (isPasswordAuthEndPoint(httpServletRequest)) {
		BasicAuthenticationConverter basicAuthenticationConverter = new BasicAuthenticationConverter();
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = basicAuthenticationConverter
			.convert(httpServletRequest);
		if (usernamePasswordAuthenticationToken == null) {
		    throw new JoanaException("Access Denied. Auth header missing in request", HttpStatus.FORBIDDEN);
		}
		authenticationManager.authenticate(basicAuthenticationConverter.convert(httpServletRequest));
	    } else {
		if (!token.isPresent()) {
		    throw new JoanaException("Access Denied. Token missing in request", HttpStatus.FORBIDDEN);
		}
		JoanaAuthenticationToken requestAuthentication = new JoanaAuthenticationToken(token.get());
		authenticationManager.authenticate(requestAuthentication);
	    }
	}
	filterChain.doFilter(request, response);
    }

}

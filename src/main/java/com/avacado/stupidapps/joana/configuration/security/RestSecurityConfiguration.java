package com.avacado.stupidapps.joana.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.avacado.stupidapps.joana.converters.JoanaUsersToProtocolConverter;

@Configuration
@EnableWebSecurity
public class RestSecurityConfiguration extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    @Autowired
    JoanaUserDetailsService joanaUserDetailsService;

    @Value("${server.api.open.endpoints}")
    private String[] openEndpoints;

    @Value("${server.api.password.endpoints}")
    private String[] passwordEndPoints;

    @Autowired
    JoanaTokenAuthenticationProvider joanaTokenAuthenticationManager;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

	http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
		.authorizeRequests().antMatchers(openEndpoints).permitAll().anyRequest().authenticated().and()
		.addFilterBefore(new JoanaTokenFilter(authenticationManager(), openEndpoints, passwordEndPoints),
			BasicAuthenticationFilter.class);
    }

    @Override
    public void configure(AuthenticationManagerBuilder authManager) throws Exception {
	authManager.userDetailsService(joanaUserDetailsService).passwordEncoder(new BCryptPasswordEncoder());
	authManager.authenticationProvider(joanaTokenAuthenticationManager);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
	registry.addConverter(new JoanaUsersToProtocolConverter());
    }

}

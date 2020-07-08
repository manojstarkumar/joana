package com.avacado.stupidapps.joana.domain;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import com.avacado.stupidapps.joana.utils.JoanaConstants;

@Valid
@Document("joana_users")
public class JoanaUser {
    @Id
    private String id;

    @NotBlank
    @Indexed(unique = true)
    private String email;

    private String name;

    @NotBlank
    private String password;

    private String authoritiesString;

    @NotBlank
    @Indexed(unique = true)
    private String xToken;

    private String pushToken;

    private List<String> tasksOwned;

    private List<String> tasksActionsNeeded;

    private List<String> pipelinesOwned;

    private List<String> pipeLinesWatched;

    public String getId() {
	return id;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public String getPassword() {
	return password;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    private String getAuthoritiesString() {
	return authoritiesString;
    }

    private void setAuthoritiesString(String authoritiesString) {
	this.authoritiesString = authoritiesString;
    }

    @Transient
    public List<GrantedAuthority> getAuthorities() {
	return AuthorityUtils.commaSeparatedStringToAuthorityList(this.getAuthoritiesString());
    }

    @Transient
    public void setAuthorities(List<GrantedAuthority> authorities) {
	this.setAuthoritiesString(StringUtils.join(authorities, JoanaConstants.AUTHORITIES_SEPARATOR));
    }

    public String getxToken() {
	return xToken;
    }

    public void setxToken(String xToken) {
	this.xToken = xToken;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public List<String> getTasksOwned() {
	return tasksOwned;
    }

    public void setTasksOwned(List<String> tasksOwned) {
	this.tasksOwned = tasksOwned;
    }

    public List<String> getTasksActionsNeeded() {
	return tasksActionsNeeded;
    }

    public void setTasksActionsNeeded(List<String> tasksActionsNeeded) {
	this.tasksActionsNeeded = tasksActionsNeeded;
    }

    @Transient
    public void addTasksActionsNeeded(String tasksId) {
	if (this.tasksActionsNeeded == null)
	    this.tasksActionsNeeded = new ArrayList<>();
	this.tasksActionsNeeded.add(tasksId);
    }
    
    @Transient
    public void removeTasksActionsNeeded(String tasksId) {
	if (this.tasksActionsNeeded == null)
	    this.tasksActionsNeeded = new ArrayList<>();
	this.tasksActionsNeeded.remove(tasksId);
    }

    public List<String> getPipelinesOwned() {
	return pipelinesOwned;
    }

    public void setPipelinesOwned(List<String> pipelinesOwned) {
	this.pipelinesOwned = pipelinesOwned;
    }

    public List<String> getPipeLinesWatched() {
	return pipeLinesWatched;
    }

    public void setPipeLinesWatched(List<String> pipeLinesWatched) {
	this.pipeLinesWatched = pipeLinesWatched;
    }

    public String getPushToken() {
	return pushToken;
    }

    public void setPushToken(String pushToken) {
	this.pushToken = pushToken;
    }

    public void setId(String id) {
	this.id = id;
    }

}

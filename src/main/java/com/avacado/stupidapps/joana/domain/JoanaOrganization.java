package com.avacado.stupidapps.joana.domain;

import org.springframework.data.mongodb.core.mapping.Document;

@Document("joana_organization")
public class JoanaOrganization {

    private String organization;
    private String name;
    
    public String getOrganization() {
        return organization;
    }
    public void setOrganization(String organization) {
        this.organization = organization;
    }
    public String getName() {
        return name;
    }
    public void setName(String dbName) {
        this.name = dbName;
    }
    
    
}

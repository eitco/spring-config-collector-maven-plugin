package de.eitco.cicd.sccmp.model;

import java.util.List;

public class ConfigurationMetadata {

    private List<Group> groups;

    private List<Property> properties;

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }
}

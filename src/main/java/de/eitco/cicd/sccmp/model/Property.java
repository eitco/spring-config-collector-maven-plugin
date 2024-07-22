package de.eitco.cicd.sccmp.model;

import org.jetbrains.annotations.NotNull;

public class Property implements Comparable<Property> {

    private String name;
    private String type;
    private String description;
    private String sourceType;
    private Object defaultValue;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public int compareTo(@NotNull Property o) {

        return name.compareTo(o.name);
    }
}

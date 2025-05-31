package com.marvinformatics.plugins.markdocs;

import java.util.List;
import java.util.ArrayList;

public class Goal {
    private String name;
    private String description;
    private String implementation;
    private String phase;
    private List<Parameter> parameters = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImplementation() {
        return implementation;
    }

    public void setImplementation(String implementation) {
        this.implementation = implementation;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    public void addParameter(Parameter parameter) {
        this.parameters.add(parameter);
    }
}
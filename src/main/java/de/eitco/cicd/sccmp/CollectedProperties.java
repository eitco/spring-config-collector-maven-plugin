package de.eitco.cicd.sccmp;

import de.eitco.cicd.sccmp.model.Property;

import java.util.List;
import java.util.Map;

public class CollectedProperties {

    private final List<String> groupNames;
    private final Map<String, List<Property>> propertiesByGroupName;

    public CollectedProperties(List<String> groupNames, Map<String, List<Property>> propertiesByGroupName) {
        this.groupNames = groupNames;
        this.propertiesByGroupName = propertiesByGroupName;
    }

    public List<String> getGroupNames() {
        return groupNames;
    }

    public Map<String, List<Property>> getPropertiesByGroupName() {
        return propertiesByGroupName;
    }
}

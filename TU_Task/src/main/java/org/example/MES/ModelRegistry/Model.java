package org.example.MES.ModelRegistry;

import lombok.*;

import java.util.HashSet;
import java.util.Set;
/*
A class which is a pojo for model
*/
@Data
public class Model {

    private String modelName;
    private String version;
    private String packageName;
    private Set<Attribute> attributes;

    public void setAttributes(Set<Attribute> attributes) {
        this.attributes.clear();
        this.attributes.addAll(attributes);
    }
}

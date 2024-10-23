package org.example.MES.ModelRegistry;

import lombok.*;

import java.util.Set;

@Data
public class Model {

    private String modelName;
    private String version;
    private String packageName;
    private Set<Attribute> attributeSet;


}

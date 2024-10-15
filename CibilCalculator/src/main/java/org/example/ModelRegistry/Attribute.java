package org.example.ModelRegistry;

import java.io.Serializable;

//This class is POJO representation of modelRegistry.json
public class Attribute implements Serializable {

    String name;
    boolean isRequired;
    boolean nullable;


    public Attribute(){}

    public String getName() {
        return this.name;
    }

    public void setName(String className) {
        this.name = className;
    }

    public boolean isRequired() {
        return isRequired;
    }

    public void setRequired(boolean required) {
        isRequired = required;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    @Override
    public String toString() {
        return "Attribute{" +
                "name='" + name + '\'' +
                ", isRequired=" + isRequired +
                ", nullable=" + nullable +
                '}';
    }
}

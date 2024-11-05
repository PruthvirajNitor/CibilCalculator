package org.example.MES.ModelRegistry;

import lombok.Data;

import java.io.Serializable;
/*
A class which is a pojo for Attributes in modelRegistry
*/
@Data
public class Attribute implements Serializable {

    String name;
    boolean required;
    boolean nullable;

    @Override
    public String toString() {
        return "Attribute{" +
                "name='" + name + '\'' +
                ", required=" + required +
                ", nullable=" + nullable +
                '}';
    }
}

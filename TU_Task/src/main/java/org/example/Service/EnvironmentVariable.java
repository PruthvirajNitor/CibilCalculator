package org.example.Service;

import lombok.Data;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

@Data
public class EnvironmentVariable {

    private final Properties properties;
    public String envFilePath="D:\\TU\\Task in maven project\\TU_Task\\task.env";
    Enumeration<?> keyList;
    Map<String,String> propertyMap = new HashMap<>();

    public EnvironmentVariable() {
        try {
            properties = new Properties();
            properties.load(new FileInputStream(envFilePath));
            keyList = properties.propertyNames();

            while (keyList.hasMoreElements()) {
                String key = (String) keyList.nextElement();
                propertyMap.put(key, properties.getProperty(key));
            }

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }

    public boolean isValidKey(String key) {
        return propertyMap.containsKey(key);
    }



}

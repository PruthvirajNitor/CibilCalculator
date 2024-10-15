package org.example.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class EnvironmentVariable {

    Properties properties =new Properties();

    public String actualDataPath ;
    public String keyDataPath;
    public String modelDataPath ;
    public String outputStoragePath ;
    public String outputParquet ;
    public String envFilePath="D:\\TU_Task\\Codes\\TU_Task 15_10\\TU_Task\\task.env";

    public EnvironmentVariable() {
        try {
            properties.load(new FileInputStream(envFilePath));
            this.actualDataPath = properties.getProperty("actualDataPath");
            this.keyDataPath = properties.getProperty("keyDataPath");
            this.modelDataPath = properties.getProperty("modelDataPath");
            this.outputStoragePath = properties.getProperty("outputStoragePath");
            this.outputParquet = properties.getProperty("outputParquet");

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }


    }



}

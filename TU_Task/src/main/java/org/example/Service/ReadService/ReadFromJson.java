package org.example.Service.ReadService;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.example.ExceptionHandler.CustomException;
import org.example.Service.SparkService.SparkSessionManager;

import java.util.Optional;
/*
    Class which implements IReadService
    to perform read operations from JSON files
*/
public class ReadFromJson implements IReadService,AutoCloseable {


    @Override
    public Optional<?> read(String filePath) throws Exception {
        return Optional.of(this.readJsonFile(filePath));
    }

    @Override
    public Optional<?> readAndMapToDataset(String filePath, Class<?> className) throws Exception {
        return Optional.of(this.readJsonFile(filePath,className));
    }


    public ReadFromJson() {}

    public Dataset<?> readJsonFile(String jsonFilePath, Class<?> className ) throws Exception {
        try {
            return SparkSessionManager.getSparkSession()
                    .read()
                    .option("multiline",true)
                    .json(jsonFilePath);
                   // .as(Encoders.bean(className));
        } catch (Exception ex) {
            {
                ex.printStackTrace();
//                throw new CustomException("invalid class Name"+ className.toString(),500);
            }
        }finally {
            this.close();
        }
        return null;

    }

    public Dataset<Row> readJsonFile(String jsonFilePath) throws Exception {
        try {
            return SparkSessionManager.getSparkSession()
                    .read()
                    .option("multiline",true)
                    .option("header", true)
                    .json(jsonFilePath);

        } catch (RuntimeException ex) {
            {
                throw new CustomException("invalid file path", 500);
            }
        } finally {
            this.close();
        }
    }

    @Override
    public void close() throws  Exception {
        SparkSessionManager.closeSession();
    }
}

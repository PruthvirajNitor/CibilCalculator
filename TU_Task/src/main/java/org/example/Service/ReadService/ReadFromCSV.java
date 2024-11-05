package org.example.Service.ReadService;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.example.ExceptionHandler.CustomException;
import org.example.Service.SparkService.SparkSessionManager;

import java.util.Optional;
/*
    Class which implements IReadService
    to perform read operations from CSV files
*/
public class ReadFromCSV implements IReadService,AutoCloseable {


    @Override
    public Optional<?> read(String filePath) throws Exception {
        return Optional.of(this.readCsvFile(filePath));
    }

    @Override
    public Optional<?> readAndMapToDataset(String filePath, Class<?> className) throws Exception {
        return Optional.of(this.readCsvFile(filePath,className));
    }


    public ReadFromCSV(){
    }

    public Dataset<?> readCsvFile(String csvFilePath, Class<?> className ) throws Exception {
        try {
            return SparkSessionManager.getSparkSession()
                    .read()
                    .option("header", true)
                    .csv(csvFilePath);
            } catch (RuntimeException ex) {
            {
                throw new CustomException("invalid class Name"+ className.toString(),500);
            }
            }finally {
            this.close();
            }
    }

    public Dataset<Row> readCsvFile(String csvFilePath) throws Exception {
        try {
            return SparkSessionManager.getSparkSession()
                    .read()
                    .option("multiline",true)
                    .option("header", true)
                    .csv(csvFilePath);

        } catch (RuntimeException ex) {
            {
                ex.printStackTrace();
//                throw new CustomException("invalid file path", 500);
            }
        } finally {
            this.close();
        }
        return null;
    }


    @Override
    public void close() throws Exception {
        SparkSessionManager.closeSession();
    }
}

package org.example.Service.WriteService;

import org.apache.hadoop.fs.PathNotFoundException;
import org.apache.spark.sql.Dataset;

import java.io.IOException;
import java.util.Optional;

public class WriteToCSVService implements IWriteService {

    @Override
    public  Optional<?> writeToFile(String outputStoragePath, Dataset<?> outputDataset) throws IOException {
        try{
            writeCsvWithOverwrite( outputStoragePath, outputDataset);
            return Optional.of(Boolean.TRUE);
        }
        catch(Exception e){
            return Optional.of(Boolean.FALSE);
        }
    }

    private static void writeCsvWithOverwrite(String outputStoragePath, Dataset<?> outputDataset) throws PathNotFoundException,IOException {
        //persisting output in csv file
        outputDataset.toDF()
                .coalesce(1)
                .write()
                .format("csv")
                .option("header", "true")
                .mode("overwrite")
                .save(outputStoragePath);
    }


}

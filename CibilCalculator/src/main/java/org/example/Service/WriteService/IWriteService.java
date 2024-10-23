package org.example.Service.WriteService;

import org.apache.spark.sql.Dataset;

import java.io.IOException;
import java.util.Optional;

public interface IWriteService {

    public Optional<?> writeToFile(String outputStoragePath, Dataset<?> outputDataset) throws IOException;

}

package org.example.Service.WriteService;

import org.apache.spark.sql.Dataset;
import org.example.MES.ModelRegistry.Attribute;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
/*
    Interface to perform Write operations
*/
public interface IWriteService {

    public Optional<?> writeToFile(String outputStoragePath, Dataset<?> outputDataset, List<Attribute> attributeList) throws IOException;

}

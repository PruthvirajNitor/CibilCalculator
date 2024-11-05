package org.example.Service.WriteService;

import org.apache.hadoop.fs.PathNotFoundException;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.example.Helper.AttributeHelper;
import org.example.MES.ModelRegistry.Attribute;
import org.example.SubjectData.OutputSubject;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
/*
    Class which implements IWriteService
    to perform write operations to CSV files
*/
public class WriteToCSVService implements IWriteService {

    @Override
    public  Optional<?> writeToFile(String outputStoragePath, Dataset<?> outputDataset,List<Attribute> attributeList) throws IOException {
        try{
            writeCsvWithOverwrite( outputStoragePath, formatToOutputDataset(outputDataset,attributeList));

            return Optional.of(Boolean.TRUE);
        }
        catch(Exception e){
            return Optional.of(Boolean.FALSE);
        }
    }

    private Dataset<OutputSubject> formatToOutputDataset(Dataset<?> outputDataset, List<Attribute> attributeList){
        outputDataset = outputDataset.as(Encoders.bean(OutputSubject.class));

        return AttributeHelper.addAttributeColumns(outputDataset,AttributeHelper.getAttributeNames(attributeList));
    }

    private static void writeCsvWithOverwrite(String outputStoragePath, Dataset<?> outputDataset) throws PathNotFoundException,IOException {

        System.out.println("total number of records : "+outputDataset.count());
        outputDataset.drop("attributeScore").select("*").show((int) outputDataset.count());

        //persisting output in csv file
        outputDataset.toDF()
                .coalesce(1)
//                .drop("attributeScore")
                .orderBy("userId")
                .write()
                .format("csv")
                .option("header", "true")
                .mode("overwrite")
                .save(outputStoragePath);
    }


}

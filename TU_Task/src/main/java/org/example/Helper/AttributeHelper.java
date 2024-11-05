package org.example.Helper;

import org.apache.spark.sql.*;
import org.example.MES.ModelRegistry.Attribute;
import org.example.MES.ModelRegistry.Model;
import org.example.SubjectData.OutputSubject;

import java.util.List;
import java.util.stream.Collectors;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.regexp_extract;

public class AttributeHelper {

    public static List<?> convertDSToList(Dataset<Class<?>> inputDataset)
    {
        return inputDataset.collectAsList();
    }

    public static Dataset<Attribute> getAttributeDSFromModelRegistry(String modelName,Dataset<Model>modelDataset){
        // Filtering the model dataset to select the model with the specified name
        Dataset<Model> unformattedModelDs=modelDataset.select("*")
                                       .where(modelDataset.col("modelName").equalTo(modelName))
                                       .as(Encoders.bean(Model.class));
        // Formatting the dataset to extract and return the attributes
        return formatAttributeDataset(unformattedModelDs);
    }

    private static Dataset<Attribute> formatAttributeDataset(Dataset<Model> modelDataset){
        // Exploding the 'attributes' column (which is assumed to be an array) into separate rows.
        // Each row will represent an individual attribute of the model.
        return modelDataset.withColumn("attributes", functions.explode(modelDataset.col("attributes")).alias("attribute"))
                    .select( functions.col("attributes.name").alias("name"),// Extracting attribute name
                             functions.col("attributes.required").alias("required"),// Extracting attribute is required
                             functions.col("attributes.nullable").alias("nullable") )//// Extracting if attribute is nullable
                    .as(Encoders.bean(Attribute.class));
        // Returning as a Dataset of Attribute objects
    }

    public static Dataset<OutputSubject> addAttributeColumns(Dataset<?> dataset, List<String> attributeNames) {

        // Converting the input dataset into a DataFrame (Row-based format).
        Dataset<Row> rowDataset = dataset.toDF();

        for (String attributeName : attributeNames) {
            //We are using withColumn() to add a new column of each attribute
            //and regexp_extract() to get the corresponding score of that particular attribute
            rowDataset = rowDataset.withColumn(attributeName,
                    regexp_extract(col("attributeScore"), attributeName + " score : ([0-9.]+)", 1));
        }
        //Converting back
        Dataset<OutputSubject> updatedDataset = rowDataset.as(Encoders.bean(OutputSubject.class));
        // Returning the updated dataset with the newly added columns.
        return updatedDataset;
    }

    public static List<String> getAttributeNames(List<Attribute> attributes) {
        return attributes.stream()
                .map(Attribute::getName)
                .collect(Collectors.toList());
    }
}

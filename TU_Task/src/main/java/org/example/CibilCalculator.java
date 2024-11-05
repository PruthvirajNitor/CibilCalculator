package org.example;
import org.apache.hadoop.util.StopWatch;
import org.apache.spark.api.java.function.FilterFunction;
import org.apache.spark.sql.*;
import org.example.ExceptionHandler.CustomException;
import org.example.Helper.AttributeHelper;
import org.example.Helper.SubjectHelper;
import org.example.MES.ModelRegistry.Attribute;
import org.example.MES.ModelRegistry.Model;
import org.example.Service.EnvironmentVariable;
import org.example.Service.ReadService.IReadService;
import org.example.Service.ReadService.ReadFromCSV;
import org.example.Service.ReadService.ReadFromJson;
import org.example.Service.SparkService.SparkSessionManager;
import org.example.Service.WriteService.IWriteService;
import org.example.Service.WriteService.WriteToCSVService;
import org.example.SubjectData.OutputSubject;
import org.example.SubjectData.Subject;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class CibilCalculator {
    public static void main(String[] args) throws Exception {


        //Input data:
        String subjectClassPath = "org.example.SubjectData.Subject";
        EnvironmentVariable env = new EnvironmentVariable();
        System.out.println("Note: 1000 ms = 1 second");
        StopWatch stopWatch = new StopWatch();
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        stopWatch.start();
        long codeStartTime = stopWatch.now(timeUnit);

//        System.setProperty ("hadoop.home.dir", "C:\\Users\\Aayush.Tailang\\Downloads\\hadoop-3.4.0\\hadoop-3.4.0\\bin\\hadoop.dll" );
//        System.load ( "C:\\Users\\Aayush.Tailang\\Downloads\\hadoop-3.4.0\\hadoop-3.4.0\\bin\\hadoop.dll");


        //creating IReadService and IWriteService instances
        IReadService csvReadService = new ReadFromCSV();
        IReadService jsonReadService = new ReadFromJson();
        IWriteService writeService = new WriteToCSVService();

         long sparkContextStartTime = stopWatch.now(timeUnit);

            System.out.println("Time Taken to create spark context: " + sparkContextStartTime + "ms");


            //Storing actual data in dataset
                Dataset<?> tempDs = (Dataset<?>) csvReadService.readAndMapToDataset(env.getPropertyMap().get("actualDataPath"), Subject.class).orElseThrow(()->new CustomException("Empty Dataset found",500));
                Dataset<Subject> actualDS = tempDs.as(Encoders.bean(Subject.class));
                
            // Storing key data in dataSet;
                tempDs = (Dataset<?>) csvReadService.read(env.getPropertyMap().get("keyDataPath")).orElseThrow(()->new CustomException("Empty Dataset found",500));
                Dataset<Row> keyDS = tempDs.as(Encoders.bean(Row.class));

            // Extract distinct user IDs from keyDataList
                Dataset<String> keyUserIdDs = keyDS.select("user_id")
                                           .as(Encoders.STRING())
                                           .distinct();

            long timeAtReadOperation = stopWatch.now(timeUnit);
            System.out.println("Time taken for read operation: " + (timeAtReadOperation-sparkContextStartTime) + "ms");

            long timeBeforeFilter = stopWatch.now(timeUnit);



            // Filter the actualData RDD based on the user IDs present in keyUserIds
                List<String> keyUserIds = keyUserIdDs.collectAsList();
                Dataset<Subject> filteredDS = actualDS.filter((FilterFunction<Subject>) row -> keyUserIds.contains(row.getUserId()));

            System.out.println("time taken for filtering : " + (stopWatch.now(timeUnit)-timeBeforeFilter)+ "ms");


         tempDs = (Dataset<?>) jsonReadService.readAndMapToDataset(env.getPropertyMap().get("mesStoragePath"), Model.class)
                .orElseThrow(()->new CustomException("Empty Dataset found",500));

        Dataset<Model> modelDataset = tempDs.as(Encoders.bean(Model.class));

       Dataset<Attribute> attributeDataset =  AttributeHelper.getAttributeDSFromModelRegistry("CIBIL_Model_B2",modelDataset);

        //Filtering model registry to only required attributes
        List<Attribute> attributeList  = attributeDataset.filter(Attribute::isRequired)
                .collectAsList();

        //Creating dataset using set
        Dataset<OutputSubject> outputDataSet = SparkSessionManager.getSparkSession().createDataset(SubjectHelper.getCibilScoreList(filteredDS.collectAsList(),attributeList,subjectClassPath)
                                                                    ,Encoders.bean(OutputSubject.class));


        long timeAtReflection =  stopWatch.now(timeUnit);
        System.out.println("Time taken for reflection: " + (timeAtReflection- timeAtReadOperation)+ "ms");

         // Write operation
        writeService.writeToFile(env.getPropertyMap().get("outputStoragePath"),outputDataSet,attributeList);


        long finalTime = stopWatch.now(timeUnit);
        System.out.println("Time taken for write operation: " + ( finalTime - timeAtReflection)+ "ms");
        System.out.println("Total Time taken: " + (finalTime - codeStartTime)+ "ms");


        SparkSessionManager.closeSession();


        }


    }


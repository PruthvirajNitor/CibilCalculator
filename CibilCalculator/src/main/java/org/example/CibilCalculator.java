package org.example;
import org.apache.hadoop.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hadoop.util.StopWatch;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FilterFunction;
import org.apache.spark.internal.config.R;
import org.apache.spark.sql.*;
import org.example.ModelRegistry.Attribute;
import org.example.Service.EnvironmentVariable;
import org.example.SubjectData.OutputSubject;
import org.example.SubjectData.Subject;
import org.example.SubjectData.SubjectHelperClass;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLOutput;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class CibilCalculator {
    public static void main(String[] args) {

//        Scanner scanner = new Scanner(System.in);
//        ObjectMapper mapper = new ObjectMapper();

        EnvironmentVariable env = new EnvironmentVariable();
        System.out.println("Note: 1000 ms = 1 second");
        StopWatch stopWatch = new StopWatch();
        TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        stopWatch.start();
        long codeStartTime = stopWatch.now(timeUnit);



        // Creating SparkSession and JavaSparkContext
        try (final var spark = SparkSession.builder()
                .appName("SparkFirstProgram")
                .master("local[*]")
                .getOrCreate();
             final var sc = new JavaSparkContext(spark.sparkContext())) {

            long sparkContextStartTime = stopWatch.now(timeUnit);
            System.setProperty ("hadoop.home.dir", "C:\\Users\\pruthviraj.jadhav\\Downloads\\hadoop-3.3.6\\hadoop-3.3.6\\bin\\hadoop.dll" );
            System.load ( "C:\\Users\\pruthviraj.jadhav\\Downloads\\hadoop-3.3.6\\hadoop-3.3.6\\bin\\hadoop.dll");

            System.out.println("Time Taken to create spark context: " + sparkContextStartTime + "ms");
            // fileIO paths
//                // file input paths stored in env file
//                String actualDataPath ="C:\\Users\\Sarvesh.Shevade\\Downloads\\actual_data.csv";
//                String dummyDataPath = "C:\\Users\\Sarvesh.Shevade\\Downloads\\more_actual_data.csv";
//
//                String keyDataPath = "C:\\Users\\Sarvesh.Shevade\\Downloads\\key_data 1.csv";
//
//                //Model registry path
//                String modelDataPath ="D:\\01.Work\\09.Project\\TU_Projects\\01.CibilCalculator\\01.Docs\\modelRegistry.json";
//                //output storage path
//                String outputStoragePath ="D:\\01.Work\\09.Project\\TU_Projects\\01.CibilCalculator\\01.Docs\\output";
//                String testPath = outputStoragePath;


//                String outputParquet = System.getProperty("outputParquet");

            //Data formatting and filtering

////            Test
//                Dataset<Row> readTestDataset = spark.read()
//                        .option("header", true)
//                        .csv(testPath);
//
//                readTestDataset.select("userId","cibilScore").sort("userId").show();

                //Storing model registry file in dataset



                //Storing input master data in dataset.
                Dataset<Subject> actualDS = spark.read()
                                                 .option("header", true)
                                                 .csv(env.actualDataPath)
                                                 .cache()
                                                 .as(Encoders.bean(Subject.class));


                Dataset<Attribute> attributeDataSet = spark.read()
                                                           .json(env.modelDataPath)
                                                           .as(Encoders.bean(Attribute.class));
                // Creating Dataset using External Dataset
                Dataset<Row> keyDS = spark.read()
                                          .option("multiline", true)
                                          .option("header", true)
                                          .csv(env.keyDataPath);

            // Extract distinct user IDs from keyDataList
            Dataset<String> keyUserIdDs = keyDS.select("user_id")
                                           .as(Encoders.STRING())
                                           .distinct();

            long timeAtReadOperation = stopWatch.now(timeUnit);
            System.out.println("Time taken for read operation: " + (timeAtReadOperation-sparkContextStartTime) + "ms");

            long timeBeforeFilter = stopWatch.now(timeUnit);

            //Filtering model registry to only required attributes
            List<Attribute> attributeList  = attributeDataSet.filter(Attribute::isRequired)
                                                             .collectAsList();

            // Filter the actualData RDD based on the user IDs present in keyUserIds
            List<String> keyUserIds = keyUserIdDs.collectAsList();
            Dataset<Subject> filteredDS = actualDS.filter((FilterFunction<Subject>) row -> keyUserIds.contains(row.getUserId()));

            System.out.println("time taken for filtering : " + (stopWatch.now(timeUnit)-timeBeforeFilter)+ "ms");


            String subjectClassPath = "org.example.SubjectData.Subject";




        //Creating dataset using set
        Dataset<OutputSubject> outputDataSet = spark.createDataset(SubjectHelperClass.getCibilScoreList(filteredDS.collectAsList(),attributeList,subjectClassPath)
                                                                    ,Encoders.bean(OutputSubject.class));

         outputDataSet.show(false);

        long timeAtReflection =  stopWatch.now(timeUnit);
        System.out.println("Time taken for reflection: " + (timeAtReflection- timeAtReadOperation)+ "ms");



         //persisting output in csv file
        outputDataSet.toDF()
                    .coalesce(1)
                    .write()
                    .format("csv")
                    .option("header", "true")
                    .mode("overwrite")
                    .save(env.outputStoragePath);


        long finalTime = stopWatch.now(timeUnit);
        System.out.println("Time taken for write operation: " + ( finalTime - timeAtReflection)+ "ms");
        System.out.println("Total Time taken: " + (finalTime - codeStartTime)+ "ms");



        }


    }

}
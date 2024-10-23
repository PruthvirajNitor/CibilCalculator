package org.example.Service.SparkService;

import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;

import java.io.Serializable;


//Singleton class
public class SparkSessionManager  {

    private static SparkSession sc;

    private SparkSessionManager(){}

    public static synchronized SparkSession getSparkSession(){
        if(sc == null){
            return getSession();
        }
        return sc;
    }

    private static SparkSession getSession()
    {
        return SparkSession.builder()
                .appName("SparkFirstProgram")
                .master("local[*]")
                .getOrCreate();
    }


    public static void closeSession(){
        if(sc != null){
            sc.stop();
        }
    }



}

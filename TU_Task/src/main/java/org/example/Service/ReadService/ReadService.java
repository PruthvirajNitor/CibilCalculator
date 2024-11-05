package org.example.Service.ReadService;


import java.io.Serializable;
import java.util.Optional;

public class ReadService implements Serializable {

    public  IReadService readFromCSV;
    public  IReadService readFromJson;

//    public Optional<?> readFromCSV(ReadFromCSV record) {
//        this.readFromCSV = record;
////        return readFromCSV.readAndMapToDataset();
//    }

}

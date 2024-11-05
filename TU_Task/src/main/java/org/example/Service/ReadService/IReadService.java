package org.example.Service.ReadService;

import java.util.Optional;
/*
    Interface to perform Read operations
*/
public interface IReadService {


    Optional<?> read(String filePath) throws Exception;

    Optional<?> readAndMapToDataset(String filePath,Class<?> className) throws Exception;

}

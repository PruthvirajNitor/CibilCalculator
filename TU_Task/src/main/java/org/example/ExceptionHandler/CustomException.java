package org.example.ExceptionHandler;

public class CustomException extends RuntimeException{

    String message;
    int errorCode;

    CustomException(){
        super();
    }
    public CustomException(String message, int errorCode){
        super(message);
        this.message = message;
        this.errorCode = errorCode;
    }
}

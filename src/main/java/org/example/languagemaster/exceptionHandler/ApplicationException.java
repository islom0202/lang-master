package org.example.languagemaster.exceptionHandler;

public class ApplicationException extends RuntimeException{
public ApplicationException(String message){
    super(message);
}
public ApplicationException(String message, Throwable ex){
    super(message, ex);
}
}

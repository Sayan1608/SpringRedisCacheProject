package com.web.mvc.exceptions;

public class NotValidEmployeeException extends RuntimeException{
    public NotValidEmployeeException(String message) {
        super(message);
    }
}

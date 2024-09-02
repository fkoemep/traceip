package com.ipinformation.exceptions;

public class ApiErrorException extends RuntimeException {

    public ApiErrorException() {
        super();
    }

    public ApiErrorException(String message) {
        super(message);
    }

}

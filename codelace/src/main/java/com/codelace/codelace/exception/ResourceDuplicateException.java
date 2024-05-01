package com.codelace.codelace.exception;

public class ResourceDuplicateException extends RuntimeException{
    public ResourceDuplicateException (){}

    public ResourceDuplicateException(String message) {
        super(message);
    }
}

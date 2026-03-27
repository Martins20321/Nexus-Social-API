package com.martinsdev.nexussocial.api.exception;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(Object id){
        super("Resource not found. Id " + id);
    }
}

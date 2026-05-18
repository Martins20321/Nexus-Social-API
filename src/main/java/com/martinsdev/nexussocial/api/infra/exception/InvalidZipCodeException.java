package com.martinsdev.nexussocial.api.infra.exception;

public class InvalidZipCodeException extends RuntimeException {
    public InvalidZipCodeException(String zipCode) {
        super("This zip code (" + zipCode + ") is invalid!");
    }
}

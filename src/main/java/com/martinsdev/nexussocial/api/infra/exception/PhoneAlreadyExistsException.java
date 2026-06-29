package com.martinsdev.nexussocial.api.infra.exception;

public class PhoneAlreadyExistsException extends RuntimeException {
    public PhoneAlreadyExistsException(String phone) {
        super("This phone already exists: " + phone);
    }
}

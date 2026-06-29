package com.martinsdev.nexussocial.api.infra.exception;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String email) {
        super("This email already exists: " + email);
    }
}

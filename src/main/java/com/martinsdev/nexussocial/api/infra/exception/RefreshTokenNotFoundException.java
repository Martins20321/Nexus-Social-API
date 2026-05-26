package com.martinsdev.nexussocial.api.infra.exception;

public class RefreshTokenNotFoundException extends RuntimeException {
    public RefreshTokenNotFoundException(String token) {
        super("This token could not be found: " + token);
    }
}

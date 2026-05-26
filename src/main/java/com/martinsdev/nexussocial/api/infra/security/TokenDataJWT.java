package com.martinsdev.nexussocial.api.infra.security;

public record TokenDataJWT(String token,
                           String refreshToken) {
}

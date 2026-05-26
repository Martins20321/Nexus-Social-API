package com.martinsdev.nexussocial.api.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.martinsdev.nexussocial.api.model.refreshtoken.RefreshToken;
import com.martinsdev.nexussocial.api.model.refreshtoken.RefreshTokenRepository;
import com.martinsdev.nexussocial.api.model.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class TokenService {

    @Value("${api.security.token.secretKey}")
    public String secretKey;
    private final RefreshTokenRepository refreshTokenRepository;

    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            return JWT.create()
                    .withIssuer("Nexus Social API")
                    .withSubject(user.getLogin())
                    .withClaim("id", user.getId())
                    .withExpiresAt(expirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error generating JWT token", exception);
        }
    }

    public String generateRefreshToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            Instant expiration = expirationDateRefreshToken();
            var refresthToken = JWT.create()
                    .withIssuer("Nexus Social API")
                    .withSubject(user.getId().toString())
                    .withExpiresAt(expiration)
                    .sign(algorithm);
            refreshTokenRepository.save(new RefreshToken(null, refresthToken, false,
                    LocalDateTime.ofInstant(expiration, ZoneOffset.of("-03:00")), user));
            return refresthToken;
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error generating Refresh token", exception);
        }
    }

    public String getSubject(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            return JWT.require(algorithm)
                    .withIssuer("Nexus Social API")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Invalid or expired JWT token");
        }
    }

    private Instant expirationDate() {
        return LocalDateTime.now().plusMinutes(30).toInstant(ZoneOffset.of("-03:00"));
    }


    private Instant expirationDateRefreshToken() {
        return LocalDateTime.now().plusDays(7).toInstant(ZoneOffset.of("-03:00"));
    }
}

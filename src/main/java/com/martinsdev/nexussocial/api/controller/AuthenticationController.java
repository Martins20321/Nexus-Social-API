package com.martinsdev.nexussocial.api.controller;

import com.martinsdev.nexussocial.api.dto.DataAuthenticationDTO;
import com.martinsdev.nexussocial.api.infra.exception.InvalidRefreshTokenException;
import com.martinsdev.nexussocial.api.infra.security.TokenDataJWT;
import com.martinsdev.nexussocial.api.infra.security.TokenService;
import com.martinsdev.nexussocial.api.infra.security.DataRefreshTokenDTO;
import com.martinsdev.nexussocial.api.model.refreshtoken.RefreshToken;
import com.martinsdev.nexussocial.api.model.refreshtoken.RefreshTokenRepository;
import com.martinsdev.nexussocial.api.model.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager manager;
    private final TokenService tokenService;
    private final RefreshTokenRepository tokenRepository;

    @PostMapping("/login")
    public ResponseEntity<TokenDataJWT> login(@RequestBody @Valid DataAuthenticationDTO dataDTO) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(dataDTO.login(), dataDTO.password());
        var authentication = manager.authenticate(authenticationToken);
        var tokenJWT = tokenService.generateToken((User) authentication.getPrincipal());
        var refreshToken = tokenService.generateRefreshToken((User) authentication.getPrincipal());

        return ResponseEntity.ok().body(new TokenDataJWT(tokenJWT, refreshToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenDataJWT> refreshToken(@RequestBody @Valid DataRefreshTokenDTO dataRefreshDTO){
        tokenService.getSubject(dataRefreshDTO.refreshToken());

        RefreshToken refreshToken = tokenRepository.findByToken(dataRefreshDTO.refreshToken())
                .orElseThrow(() -> new InvalidRefreshTokenException("Refresh token Not Found: " + dataRefreshDTO.refreshToken()));

        if (refreshToken.isUsed()){
            throw new InvalidRefreshTokenException("This token has already been used");
        }

        if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now())){
            throw new InvalidRefreshTokenException("This token has expired");
        }

        refreshToken.setUsed(true);
        tokenRepository.save(refreshToken);

        User user = refreshToken.getUser();
        var updatedTokenJWT = tokenService.generateToken(user);
        var updatedRefreshToken = tokenService.generateRefreshToken(user);


        return ResponseEntity.ok().body(new TokenDataJWT(updatedTokenJWT, updatedRefreshToken));
    }
}

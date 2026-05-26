package com.martinsdev.nexussocial.api.controller;

import com.martinsdev.nexussocial.api.dto.DataAuthenticationDTO;
import com.martinsdev.nexussocial.api.infra.security.TokenDataJWT;
import com.martinsdev.nexussocial.api.infra.security.TokenService;
import com.martinsdev.nexussocial.api.model.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/login")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager manager;
    private final TokenService tokenService;

    @PostMapping
    public ResponseEntity<TokenDataJWT> login(@RequestBody DataAuthenticationDTO dataDTO) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(dataDTO.login(), dataDTO.password());
        var authentication = manager.authenticate(authenticationToken);
        var tokenJWT = tokenService.generateToken((User) authentication.getPrincipal());

        return ResponseEntity.ok().body(new TokenDataJWT(tokenJWT));
    }
}

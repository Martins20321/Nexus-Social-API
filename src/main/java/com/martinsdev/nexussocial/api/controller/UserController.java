package com.martinsdev.nexussocial.api.controller;

import com.martinsdev.nexussocial.api.dto.UpdateRequestUserDTO;
import com.martinsdev.nexussocial.api.dto.UserResponseDTO;
import com.martinsdev.nexussocial.api.model.user.User;
import com.martinsdev.nexussocial.api.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-key")
public class UserController {

    private final UserService service;

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> findById(@AuthenticationPrincipal User client){
        return ResponseEntity.ok(service.findMe(client));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> update(@PathVariable Long id, @RequestBody @Valid UpdateRequestUserDTO dto, @AuthenticationPrincipal User donor){
        return ResponseEntity.ok(service.update(id, dto, donor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable Long id, @AuthenticationPrincipal User donor){
        service.deactivate(id, donor);
        return ResponseEntity.noContent().build();
    }
}

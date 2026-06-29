package com.martinsdev.nexussocial.api.dto;

import com.martinsdev.nexussocial.api.model.user.User;

import java.time.LocalDateTime;

public record UserResponseDTO(Long id,
                              String name,
                              String email,
                              String phone,
                              String role,
                              LocalDateTime createdAt,
                              boolean enabled) {

    public UserResponseDTO(User user){
        this(user.getId(), user.getName(), user.getEmail(), user.getPhone(), user.getRole().name(), user.getCreatedAt(), user.isEnabled());
    }
}

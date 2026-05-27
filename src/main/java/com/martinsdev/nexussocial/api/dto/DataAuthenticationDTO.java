package com.martinsdev.nexussocial.api.dto;

import jakarta.validation.constraints.NotBlank;

public record DataAuthenticationDTO(@NotBlank String login,
                                    @NotBlank String password) {
}

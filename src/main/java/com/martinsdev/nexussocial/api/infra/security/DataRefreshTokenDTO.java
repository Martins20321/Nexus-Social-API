package com.martinsdev.nexussocial.api.infra.security;

import jakarta.validation.constraints.NotBlank;

public record DataRefreshTokenDTO(@NotBlank String refreshToken) {
}

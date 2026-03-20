package com.martinsdev.nexussocial.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record InsertDonorDTO(@NotBlank String name,
                             @NotBlank String phone,
                             @NotBlank @Email String email) {
}

package com.martinsdev.nexussocial.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RegisterRequestUserDTO(@NotBlank String name,
                                     @NotBlank @Email String email,
                                     @NotBlank String password,
                                     @NotBlank @Pattern(regexp = "\\(?\\d{2}\\)?\\d?\\d{4}-?\\d{4}", message = "The phone format is invalid")
                                     String phone) {
}

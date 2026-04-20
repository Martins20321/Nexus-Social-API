package com.martinsdev.nexussocial.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record InsertInstitutionDTO(@NotBlank String name,
                                   @NotBlank String cnpj,
                                   @Pattern(regexp = "\\(?\\d{2}\\)?\\d?\\d{4}-?\\d{4}", message = "The phone format is invalid")
                                   String phone,
                                   @NotBlank @Email(message = "The email format is invalid") String email,
                                   @NotNull Long addressId) {
}

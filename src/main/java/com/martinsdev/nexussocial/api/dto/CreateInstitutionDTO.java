package com.martinsdev.nexussocial.api.dto;

import com.martinsdev.nexussocial.api.model.enums.AreaOfActivity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreateInstitutionDTO(@NotBlank String userName,
                                   @NotBlank String userEmail,
                                   @NotBlank String userPassword,
                                   @NotBlank String name,
                                   @NotBlank String cnpj,
                                   @NotNull AreaOfActivity areaOfActivity,
                                   @Pattern(regexp = "\\(?\\d{2}\\)?\\d?\\d{4}-?\\d{4}", message = "The phone format is invalid")
                                   String phone,
                                   @NotBlank @Email(message = "The email format is invalid") String email,
                                   @NotNull String zipCode,
                                   @NotNull String number) {
}

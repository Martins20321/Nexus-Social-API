package com.martinsdev.nexussocial.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UpdateRequestUserDTO(String name,
                            @Pattern(regexp = "\\(?\\d{2}\\)?\\d?\\d{4}-?\\d{4}", message = "The phone format is invalid")
                            String phone,
                            @Email(message = "The email format is invalid") String email) {
}

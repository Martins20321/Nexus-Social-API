package com.martinsdev.nexussocial.api.dto;

import jakarta.validation.constraints.Pattern;

public record UpdateInstitutionDTO(
        @Pattern(regexp = "\\(?\\d{2}\\)?\\d?\\d{4}-?\\d{4}", message = "The phone format is invalid")
        String phone) {
}

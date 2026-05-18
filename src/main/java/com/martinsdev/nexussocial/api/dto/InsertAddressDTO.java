package com.martinsdev.nexussocial.api.dto;

import jakarta.validation.constraints.NotBlank;

public record InsertAddressDTO(@NotBlank String zipCode,
                               @NotBlank String number) {
}

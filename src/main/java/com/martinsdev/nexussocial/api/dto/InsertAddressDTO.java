package com.martinsdev.nexussocial.api.dto;

import jakarta.validation.constraints.NotBlank;

public record InsertAddressDTO(@NotBlank String street,
                               @NotBlank String number,
                               @NotBlank String neighborhood,
                               @NotBlank String city,
                               @NotBlank String state) {
}

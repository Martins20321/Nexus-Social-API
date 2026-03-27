package com.martinsdev.nexussocial.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record InsertAddressDTO(@NotBlank String street,
                               @NotBlank String number,
                               @NotBlank String neighborhood,
                               @NotBlank String city,
                               @Size(min = 2, max = 2)
                               @NotBlank String state) {
}

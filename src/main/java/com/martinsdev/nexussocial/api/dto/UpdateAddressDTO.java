package com.martinsdev.nexussocial.api.dto;

import jakarta.validation.constraints.Size;

public record UpdateAddressDTO(String street,
                               String number,
                               String neighborhood,
                               String city,
                               @Size(min = 2, max = 2)
                               String state) {
}

package com.martinsdev.nexussocial.api.dto;

public record AddressDTO(Long id,
                         String street,
                         String number,
                         String neighborhood,
                         String city,
                         String state) {
}

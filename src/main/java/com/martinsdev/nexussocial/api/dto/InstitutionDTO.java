package com.martinsdev.nexussocial.api.dto;

import com.martinsdev.nexussocial.api.model.Address;

public record InstitutionDTO(Long id,
                             String name,
                             String cnpj,
                             String email,
                             Address address) {
}

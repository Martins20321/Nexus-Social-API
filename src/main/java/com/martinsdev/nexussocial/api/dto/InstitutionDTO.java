package com.martinsdev.nexussocial.api.dto;

import com.martinsdev.nexussocial.api.model.Institution;

public record InstitutionDTO(Long id,
                             String name,
                             String cnpj,
                             String phone,
                             String email,
                             AddressDTO adress) {

    public InstitutionDTO(Institution institution){
        this(institution.getId(), institution.getName(), institution.getCnpj(), institution.getPhone(), institution.getEmail(),new AddressDTO(institution.getAddress()));
    }
}

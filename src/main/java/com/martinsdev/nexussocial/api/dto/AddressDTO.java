package com.martinsdev.nexussocial.api.dto;

import com.martinsdev.nexussocial.api.model.Address;

public record AddressDTO(Long id,
                         String zipCode,
                         String street,
                         String number,
                         String neighborhood,
                         String city,
                         String state) {

    public AddressDTO(Address address){
        this(address.getId(), address.getZipCode(), address.getStreet(), address.getNumber(), address.getNeighborhood(), address.getCity(), address.getState());
    }
}

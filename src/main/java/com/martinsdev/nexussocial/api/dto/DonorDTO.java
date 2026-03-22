package com.martinsdev.nexussocial.api.dto;

import com.martinsdev.nexussocial.api.model.Donor;

public record DonorDTO(String name,
                       String phone,
                       String email) {

    public DonorDTO(Donor donor){
        this(donor.getName(), donor.getPhone(), donor.getEmail());
    }
}

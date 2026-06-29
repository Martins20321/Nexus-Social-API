package com.martinsdev.nexussocial.api.dto;

import com.martinsdev.nexussocial.api.model.Donation;

public record DonationDTO(Long id,
                          Integer donatedQuantity,
                          NecessityDTO necessity,
                          UserResponseDTO donor) {

    public DonationDTO(Donation donation){
        this(donation.getId(), donation.getDonatedQuantity(), new NecessityDTO(donation.getNecessity()), new UserResponseDTO(donation.getDonor()));
    }
}

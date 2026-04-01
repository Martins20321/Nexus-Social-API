package com.martinsdev.nexussocial.api.dto;

import com.martinsdev.nexussocial.api.model.Donation;

public record DonationDTO(Long id,
                          Integer donatedQuantity,
                          NecessityDTO necessityDTO,
                          DonorDTO donorDTO) {

    public DonationDTO(Donation donation){
        this(donation.getId(), donation.getDonatedQuantity(), new NecessityDTO(donation.getNecessity()), new DonorDTO(donation.getDonor()));
    }
}

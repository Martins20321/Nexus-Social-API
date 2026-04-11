package com.martinsdev.nexussocial.api.service;

import com.martinsdev.nexussocial.api.dto.DonationDTO;
import com.martinsdev.nexussocial.api.dto.InsertDonationDTO;
import com.martinsdev.nexussocial.api.dto.UpdateDonationDTO;
import com.martinsdev.nexussocial.api.exception.ResourceNotFoundException;
import com.martinsdev.nexussocial.api.model.Donation;
import com.martinsdev.nexussocial.api.repository.DonationRepository;
import com.martinsdev.nexussocial.api.repository.DonorRepository;
import com.martinsdev.nexussocial.api.repository.NecessityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DonationService {

    private final DonationRepository repository;
    private final DonorRepository donorRepository;
    private final NecessityRepository necessityRepository;

    public List<DonationDTO> findAll() {
        return repository.findAll().stream().map(DonationDTO::new).toList();
    }

    public DonationDTO findById(Long id) {
        return repository.findById(id).map(DonationDTO::new).orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @Transactional
    public DonationDTO insert(InsertDonationDTO dto) {
        var donor = donorRepository.findById(dto.idDonor()).orElseThrow(() -> new ResourceNotFoundException(dto.idDonor()));
        var necessity = necessityRepository.findById(dto.idNecessity()).orElseThrow(() -> new ResourceNotFoundException(dto.idNecessity()));

        necessity.addDonation(dto.donatedQuantity());

        Donation donation = new Donation(dto, donor, necessity);

        donation = repository.save(donation);
        return new DonationDTO(donation);
    }

    @Transactional
    public DonationDTO update(Long id, UpdateDonationDTO dto) {
        Donation donation = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));

        int dif = dto.donatedQuantity() - donation.getDonatedQuantity();
        donation.getNecessity().addDonation(dif);

        donation.updateData(dto);
        repository.save(donation);
        return new DonationDTO(donation);
    }

    @Transactional
    public void delete(Long id) {
        Donation donation = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        donation.getNecessity().removeDonation(donation.getDonatedQuantity());

        repository.delete(donation);
    }
}

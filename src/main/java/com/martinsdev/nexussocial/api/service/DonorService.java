package com.martinsdev.nexussocial.api.service;

import com.martinsdev.nexussocial.api.dto.DonorDTO;
import com.martinsdev.nexussocial.api.dto.InsertDonorDTO;
import com.martinsdev.nexussocial.api.dto.UpdateDonorDTO;
import com.martinsdev.nexussocial.api.exception.ResourceNotFoundException;
import com.martinsdev.nexussocial.api.exception.ValidationException;
import com.martinsdev.nexussocial.api.model.Donor;
import com.martinsdev.nexussocial.api.repository.DonorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DonorService {

    private final DonorRepository repository;

    public List<DonorDTO> findAll() {
        return repository.findAll().stream().map(DonorDTO::new).toList();
    }

    public DonorDTO findById(Long id) {
        return repository.findById(id).map(DonorDTO::new).orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @Transactional
    public void insert(InsertDonorDTO dto) {
        boolean alreadyExists = repository.existsByNameOrEmail(dto.name(), dto.email());

        if (alreadyExists) {
            throw new ValidationException("This donor already exists");
        } else {
            repository.save(new Donor(dto));
        }
    }

    @Transactional
    public DonorDTO update(UpdateDonorDTO dto) {
        Donor donor = repository.findById(dto.id())
                .orElseThrow(() -> new ResourceNotFoundException(dto.id()));
        donor.updateData(dto);
        repository.save(donor);
        return new DonorDTO(donor);
    }

    @Transactional
    public void delete(Long id){
        Donor donor = repository.findById(id)
                .orElseThrow(() -> new ValidationException("Donor Not found"));
        repository.delete(donor);
    }
}

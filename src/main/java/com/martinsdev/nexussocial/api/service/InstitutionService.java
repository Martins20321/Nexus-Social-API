package com.martinsdev.nexussocial.api.service;

import com.martinsdev.nexussocial.api.dto.InsertInstitutionDTO;
import com.martinsdev.nexussocial.api.dto.InstitutionDTO;
import com.martinsdev.nexussocial.api.dto.UpdateInstitutionDTO;
import com.martinsdev.nexussocial.api.exception.ValidationException;
import com.martinsdev.nexussocial.api.model.Institution;
import com.martinsdev.nexussocial.api.repository.InstitutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InstitutionService {

    private final InstitutionRepository repository;

    public List<InstitutionDTO> findAll() {
        return repository.findAll().stream().map(InstitutionDTO::new).toList();
    }

    public InstitutionDTO findById(Long id) {
        return repository.findById(id).map(InstitutionDTO::new)
                .orElseThrow(() -> new ValidationException("Institution Not Found"));
    }

    public void insert(InsertInstitutionDTO dto) {
        boolean alreadyExists = repository.existsByNameOrCnpj(dto.name(), dto.cnpj());

        if (alreadyExists) {
            throw new ValidationException("This institution already exists");
        } else {
            repository.save(new Institution(dto));
        }
    }

    public void update(UpdateInstitutionDTO dto) {
        Institution institution = repository.findById(dto.id())
                .orElseThrow(() -> new ValidationException("Institution Not Found"));
        institution.updateData(dto);
        repository.save(institution);
    }

    public void delete(Long id){

        Institution institution = repository.findById(id)
                .orElseThrow(() -> new ValidationException("Institution Not Found"));
        repository.delete(institution);
    }
}

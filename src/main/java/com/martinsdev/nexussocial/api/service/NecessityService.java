package com.martinsdev.nexussocial.api.service;

import com.martinsdev.nexussocial.api.dto.InsertNecessityDTO;
import com.martinsdev.nexussocial.api.dto.InstitutionDTO;
import com.martinsdev.nexussocial.api.dto.NecessityDTO;
import com.martinsdev.nexussocial.api.dto.UpdateNecessityDTO;
import com.martinsdev.nexussocial.api.exception.ValidationException;
import com.martinsdev.nexussocial.api.model.Necessity;
import com.martinsdev.nexussocial.api.repository.InstitutionRepository;
import com.martinsdev.nexussocial.api.repository.NecessityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NecessityService {

    private final NecessityRepository repository;
    private final InstitutionRepository institutionRepository;

    public List<NecessityDTO> findAll() {
        return repository.findAll().stream().map(NecessityDTO::new).toList();
    }

    public NecessityDTO findById(Long id){
        return repository.findById(id).map(NecessityDTO::new)
                .orElseThrow(() -> new ValidationException("Necessity Not Found"));
    }

    public void insert(InsertNecessityDTO dto){
        var institution = institutionRepository.findById(dto.idInstitution())
                .orElseThrow(() -> new ValidationException("Institution not found with ID: " + dto.idInstitution()));

        repository.save(new Necessity(dto, institution));
    }

    public void update(UpdateNecessityDTO dto){
        Necessity necessity = repository.findById(dto.id())
                .orElseThrow(() -> new ValidationException("Necessity Not Found"));
        necessity.updateData(dto);
        repository.save(necessity);
    }

    public void delete(Long id){
        Necessity necessity = repository.findById(id).orElseThrow(() -> new ValidationException("Necessity Not Found"));
        repository.delete(necessity);
    }
}

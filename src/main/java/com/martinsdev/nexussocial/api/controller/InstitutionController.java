package com.martinsdev.nexussocial.api.controller;

import com.martinsdev.nexussocial.api.dto.InsertInstitutionDTO;
import com.martinsdev.nexussocial.api.dto.InstitutionDTO;
import com.martinsdev.nexussocial.api.dto.UpdateInstitutionDTO;
import com.martinsdev.nexussocial.api.service.InstitutionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/institutions")
@RequiredArgsConstructor
public class InstitutionController {

    private final InstitutionService service;

    @GetMapping
    public ResponseEntity<List<InstitutionDTO>> findAll() {
        List<InstitutionDTO> institutions = service.findAll();
        return ResponseEntity.ok().body(institutions);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<InstitutionDTO> findById(@PathVariable Long id) {
        InstitutionDTO institution = service.findById(id);
        return ResponseEntity.ok().body(institution);
    }

    @PostMapping
    public ResponseEntity<InstitutionDTO> insert(@RequestBody @Valid InsertInstitutionDTO dto) {
        InstitutionDTO institution = service.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(institution.id()).toUri();
        return ResponseEntity.created(uri).body(institution);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InstitutionDTO> updateById(@PathVariable Long id, @RequestBody @Valid UpdateInstitutionDTO dto) {
        InstitutionDTO institution = service.update(id, dto);
        return ResponseEntity.ok().body(institution);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

package com.martinsdev.nexussocial.api.controller;

import com.martinsdev.nexussocial.api.dto.InsertNecessityDTO;
import com.martinsdev.nexussocial.api.dto.NecessityDTO;
import com.martinsdev.nexussocial.api.dto.UpdateNecessityDTO;
import com.martinsdev.nexussocial.api.service.NecessityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/necessities")
@RequiredArgsConstructor
public class NecessityController {

    private final NecessityService service;

    @GetMapping
    public ResponseEntity<List<NecessityDTO>> findAll() {
        List<NecessityDTO> necessities = service.findAll();
        return ResponseEntity.ok().body(necessities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NecessityDTO> findById(@PathVariable Long id) {
        NecessityDTO necessity = service.findById(id);
        return ResponseEntity.ok().body(necessity);
    }

    @PostMapping
    public ResponseEntity<NecessityDTO> insert(@RequestBody @Valid InsertNecessityDTO dto) {
        NecessityDTO necessity = service.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(necessity.id()).toUri();
        return ResponseEntity.created(uri).body(necessity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NecessityDTO> update(@PathVariable Long id, @RequestBody @Valid UpdateNecessityDTO dto) {
        NecessityDTO necessity = service.update(id, dto);
        return ResponseEntity.ok().body(necessity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

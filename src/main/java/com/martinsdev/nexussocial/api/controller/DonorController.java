package com.martinsdev.nexussocial.api.controller;

import com.martinsdev.nexussocial.api.dto.DonorDTO;
import com.martinsdev.nexussocial.api.dto.InsertDonorDTO;
import com.martinsdev.nexussocial.api.dto.UpdateDonorDTO;
import com.martinsdev.nexussocial.api.service.DonorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/donors")
@RequiredArgsConstructor
public class DonorController {

    private final DonorService service;

    @GetMapping
    public ResponseEntity<List<DonorDTO>> findAll(){
        List<DonorDTO> donors = service.findAll();
        return ResponseEntity.ok().body(donors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DonorDTO> findById(@PathVariable Long id){
        DonorDTO donor = service.findById(id);
        return ResponseEntity.ok().body(donor);
    }

    @PostMapping
    public ResponseEntity<DonorDTO> insert(@RequestBody @Valid InsertDonorDTO dto){
        DonorDTO donor = service.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(donor.id()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<DonorDTO> update(@PathVariable Long id, @RequestBody @Valid UpdateDonorDTO dto){
        DonorDTO donor = service.update(id, dto);
        return ResponseEntity.ok().body(donor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

package com.martinsdev.nexussocial.api.controller;

import com.martinsdev.nexussocial.api.dto.DonationDTO;
import com.martinsdev.nexussocial.api.dto.InsertDonationDTO;
import com.martinsdev.nexussocial.api.dto.UpdateDonationDTO;
import com.martinsdev.nexussocial.api.service.DonationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/donations")
@RequiredArgsConstructor
public class DonationController {

    private final DonationService service;

    @GetMapping
    public ResponseEntity<List<DonationDTO>> findAll(){
        List<DonationDTO> donations = service.findAll();
        return ResponseEntity.ok().body(donations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DonationDTO> findById(@PathVariable Long id){
        DonationDTO donation = service.findById(id);
        return ResponseEntity.ok().body(donation);
    }

    @PostMapping
    public ResponseEntity<DonationDTO> insert(@RequestBody @Valid InsertDonationDTO dto){
        DonationDTO donation = service.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(donation.id()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<DonationDTO> update(@PathVariable Long id, @RequestBody @Valid UpdateDonationDTO dto){
        DonationDTO donation = service.update(id, dto);
        return ResponseEntity.ok().body(donation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

package com.martinsdev.nexussocial.api.service;

import com.martinsdev.nexussocial.api.dto.AddressDTO;
import com.martinsdev.nexussocial.api.dto.InsertAddressDTO;
import com.martinsdev.nexussocial.api.dto.UpdateAddressDTO;
import com.martinsdev.nexussocial.api.exception.ValidationException;
import com.martinsdev.nexussocial.api.model.Address;
import com.martinsdev.nexussocial.api.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository repository;

    public List<AddressDTO> findAll() {
        return repository.findAll().stream().map(AddressDTO::new).toList();
    }

    public AddressDTO findById(Long id) {
        return repository.findById(id).map(AddressDTO::new)
                .orElseThrow(() -> new ValidationException("This Address Not Found"));
    }

    @Transactional
    public AddressDTO insert(InsertAddressDTO dto) {
        Address address = new Address(dto);
        address.setState(address.getState().toUpperCase());
        address = repository.save(address);
        return new AddressDTO(address);
    }

    @Transactional
    public AddressDTO update(Long id, UpdateAddressDTO dto){
        Address address = repository.findById(id)
                .orElseThrow(() -> new ValidationException("This Address Not Found"));
        address.updateData(dto);
        repository.save(address);
        return new AddressDTO(address);
    }

    @Transactional
    public void delete(Long id){
        Address address = repository.findById(id).orElseThrow(() -> new ValidationException("This Address Not Found"));
        repository.delete(address);
    }
}

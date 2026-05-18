package com.martinsdev.nexussocial.api.service;

import com.martinsdev.nexussocial.api.dto.InsertAddressDTO;
import com.martinsdev.nexussocial.api.dto.UpdateAddressDTO;
import com.martinsdev.nexussocial.api.dto.ViaCepResponseDTO;
import com.martinsdev.nexussocial.api.infra.client.ViaCepClient;
import com.martinsdev.nexussocial.api.infra.exception.ResourceNotFoundException;
import com.martinsdev.nexussocial.api.model.Address;
import com.martinsdev.nexussocial.api.repository.AddressRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    @InjectMocks
    private AddressService service;

    @Mock
    private AddressRepository repository;

    @Mock
    private ViaCepClient cepClient;

    private InsertAddressDTO inAddressDTO;

    @Mock
    private UpdateAddressDTO upAddressDTO;

    @Captor
    private ArgumentCaptor<Address> addressCaptor;

    @Test
    @DisplayName("It should save the address when registering")
    void verificationSuccessAddressRegistration() {

        //ARRANGE
        this.inAddressDTO = new InsertAddressDTO("01001000", "91");

        //Simulating the return from the ViaCep API
        ViaCepResponseDTO responseDTO = new ViaCepResponseDTO("01001-000", "Praça da Sé",
                "Sé", "São Paulo", "SP", false);
        Address addressMockado = new Address(responseDTO.zipCode(), responseDTO.street(), inAddressDTO.number(),
                responseDTO.neighborhood(), responseDTO.city(), responseDTO.state());
        addressMockado.setId(1l); //Simulating that the bank generated ID 1

        when(repository.save(ArgumentMatchers.any(Address.class))).thenReturn(addressMockado);
        when(cepClient.findAddressByZipCode("01001000")).thenReturn(responseDTO);

        //ACT
        var result = service.insert(inAddressDTO);

        //ASSERT
        then(repository).should().save(addressCaptor.capture());
        Address savedAddress = addressCaptor.getValue();
        Assertions.assertEquals(savedAddress.getZipCode(), result.zipCode());
        Assertions.assertEquals(addressMockado.getId(), result.id());
    }

    @Test
    @DisplayName("It should not save the address when registering. Ex: invalid values")
    void verificationErrorAddressRegistration() {

        //ARRANGE
        this.inAddressDTO = new InsertAddressDTO("01001000", "91");
        ViaCepResponseDTO responseDTO = new ViaCepResponseDTO("01001-000", "Praça da Sé",
                "Sé", "São Paulo", "SP", false);


        //repository configured to throw exception
        when(cepClient.findAddressByZipCode("01001000")).thenReturn(responseDTO);
        when(repository.save(any(Address.class))).thenThrow(RuntimeException.class);

        //ASSERT + ACT
        Assertions.assertThrows(RuntimeException.class, () -> service.insert(inAddressDTO));
    }

    @Test
    @DisplayName("It should save the address when update")
    void verificationSuccessAddressUpdate() {

        //ARRANGE
        Long existingId = 1l;
        Address oldAddress = new Address();
        oldAddress.setId(existingId);
        oldAddress.setStreet("street");
        oldAddress.setCity("Ipanema");
        oldAddress.setState("RJ");

        this.upAddressDTO = new UpdateAddressDTO(null, null, "Taguatinga", "Brasília", "DF");

        when(repository.findById(existingId)).thenReturn(Optional.of(oldAddress));
        when(repository.save(ArgumentMatchers.any(Address.class))).thenAnswer(i -> i.getArgument(0));

        //ACT
        var result = service.update(existingId, upAddressDTO);

        //ASSERT
        then(repository).should().findById(existingId);
        then(repository).should().save(addressCaptor.capture());
        Address updatedAddress = addressCaptor.getValue();

        Assertions.assertEquals("Brasília", updatedAddress.getCity());
        Assertions.assertEquals("DF", updatedAddress.getState());

        Assertions.assertEquals("Brasília", result.city());
    }

    @Test
    @DisplayName("An exception should be thrown when attempting to update a non-existent ID")
    void verificationAddressUpdateFailure() {

        //ARRANGE
        Long nonExistentId = 90l;
        this.upAddressDTO = new UpdateAddressDTO(null, null, "Taguatinga", "Brasília", "DF");

        when(repository.findById(nonExistentId)).thenReturn(Optional.empty());

        //ASSERT + ACT
        //checking if the exception was called
        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.update(nonExistentId, upAddressDTO));

        //ensuring that the save method should never be called
        then(repository).should(Mockito.never()).save(any());
    }

    @Test
    void verificationSuccessAddressDelete() {

        //ARRANGE
        Long existingId = 1l;
        Address address = new Address();
        address.setId(existingId);

        when(repository.findById(existingId)).thenReturn(Optional.of(address));

        //ACT
        service.delete(existingId);

        //ASSERT
        then(repository).should().findById(existingId);
        then(repository).should().delete(addressCaptor.capture());
        Assertions.assertEquals(existingId, addressCaptor.getValue().getId());
    }

    @Test
    void verificationErrorAddressDelete() {

        //ARRANGE
        Long nonExistentId = 90l;

        when(repository.findById(nonExistentId)).thenReturn(Optional.empty());

        //ASSERT + ACT
        //checking if the exception was called
        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.delete(nonExistentId));

        //ensuring that the delete method should never be called.
        then(repository).should(Mockito.never()).delete(any());

    }
}
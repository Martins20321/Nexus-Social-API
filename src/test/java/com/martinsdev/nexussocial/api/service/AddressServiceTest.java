package com.martinsdev.nexussocial.api.service;

import com.martinsdev.nexussocial.api.dto.InsertAddressDTO;
import com.martinsdev.nexussocial.api.dto.UpdateAddressDTO;
import com.martinsdev.nexussocial.api.exception.ResourceNotFoundException;
import com.martinsdev.nexussocial.api.model.Address;
import com.martinsdev.nexussocial.api.repository.AddressRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    @InjectMocks
    private AddressService service;

    @Mock
    private AddressRepository repository;

    private InsertAddressDTO inAddressDTO;

    @Mock
    private UpdateAddressDTO upAddressDTO;

    @Captor
    private ArgumentCaptor<Address> addressCaptor;

    @Test
    @DisplayName("It should save the address when registering")
    void verificationSuccessAddressRegistration() {

        //ARRANGE
        this.inAddressDTO = new InsertAddressDTO("Maple Street", "123A", "Downtown", "New York", "NY");
        Address addressMockado = new Address(inAddressDTO);
        addressMockado.setId(1l); //Simulating that the bank generated ID 1

        when(repository.save(ArgumentMatchers.any(Address.class))).thenReturn(addressMockado);

        //ACT
        var result = service.insert(inAddressDTO);

        //ASSERT
        then(repository).should().save(addressCaptor.capture());
        Address savedAddress = addressCaptor.getValue();
        Assertions.assertEquals(inAddressDTO.state(), savedAddress.getState());
        Assertions.assertEquals(addressMockado.getId(), result.id());
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

}
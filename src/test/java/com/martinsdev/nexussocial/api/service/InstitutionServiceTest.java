package com.martinsdev.nexussocial.api.service;

import com.martinsdev.nexussocial.api.dto.InsertInstitutionDTO;
import com.martinsdev.nexussocial.api.dto.UpdateInstitutionDTO;
import com.martinsdev.nexussocial.api.exception.ResourceNotFoundException;
import com.martinsdev.nexussocial.api.exception.ValidationException;
import com.martinsdev.nexussocial.api.model.Address;
import com.martinsdev.nexussocial.api.model.Institution;
import com.martinsdev.nexussocial.api.repository.AddressRepository;
import com.martinsdev.nexussocial.api.repository.InstitutionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InstitutionServiceTest {

    @InjectMocks
    private InstitutionService service;

    @Mock
    private InstitutionRepository repository;

    @Mock
    private AddressRepository addressRepository;

    private InsertInstitutionDTO inInstitutionDTO;
    private UpdateInstitutionDTO upInstitutionDTO;

    @Captor
    private ArgumentCaptor<Institution> institutionCaptor;

    @Test
    @DisplayName("It should save the institution when registering")
    void verificationSucessInstitutionRegistration() {

        //ARRANGE
        Address addressMockado = new Address(2l, "Maple Street", "123A", "Downtown", "New York", "NY");

        this.inInstitutionDTO = new InsertInstitutionDTO("ONG RN", "12.345.678/0001-95", "(81)98321-5423",
                "ongrn@email.com", addressMockado.getId());
        Institution institutionMockado = new Institution(inInstitutionDTO);
        institutionMockado.setId(1l); //Simulating that the bank generated ID 1

        when(repository.save(ArgumentMatchers.any(Institution.class))).thenReturn(institutionMockado);

        when(repository.existsByNameOrCnpj(institutionMockado.getName(), institutionMockado.getCnpj())).thenReturn(false);
        when(addressRepository.findById(addressMockado.getId())).thenReturn(Optional.of(addressMockado));
        institutionMockado.setAddress(addressMockado);

        //ACT
        var result = service.insert(inInstitutionDTO);

        //ASSERT
        then(repository).should().save(institutionCaptor.capture());
        Institution savedInstitution = institutionCaptor.getValue();

        Assertions.assertEquals(inInstitutionDTO.cnpj(), savedInstitution.getCnpj());
        Assertions.assertEquals(institutionMockado.getId(), result.id());
        Assertions.assertEquals(addressMockado, savedInstitution.getAddress());
    }

    @Test
    @DisplayName("It should not save the Institution when registering. Ex: Institution already Exists")
    void verificationErrorAddressRegistration() {

        //ARRANGE
        Address addressMockado = new Address(2l, "Maple Street", "123A", "Downtown", "New York", "NY");

        this.inInstitutionDTO = new InsertInstitutionDTO("ONG RN", "12.345.678/0001-95", "(81)98321-5423",
                "ongrn@email.com", addressMockado.getId());

        when(repository.existsByNameOrCnpj(anyString(), anyString())).thenReturn(true);

        //ASSERT + ACT
        ValidationException exceptionActual = Assertions.assertThrows(ValidationException.class, () -> service.insert(inInstitutionDTO));
        then(repository).should(Mockito.never()).save(any());
        //comparing the message that is inside the exception
        Assertions.assertEquals("This institution already exists", exceptionActual.getMessage());

    }

    @Test
    @DisplayName("It should save the institution when update")
    void verificationSuccessInsitutionUpdate() {

        //ARRANGE
        Long existingId = 1l;
        Institution oldInstitution = new Institution();
        oldInstitution.setId(existingId);
        oldInstitution.setName("Bright Future Center");
        oldInstitution.setCnpj("98.765.432/0001-10");
        oldInstitution.setPhone("(21)99876-5432");
        oldInstitution.setEmail("info@brightfuture.org");

        Address address = new Address();
        address.setStreet("Pine Street");
        address.setNumber("456");
        address.setNeighborhood("Brooklyn Heights");
        address.setCity("New York");
        address.setState("NY");

        oldInstitution.setAddress(address);

        this.upInstitutionDTO = new UpdateInstitutionDTO("Tech Institute", "(11)91234-5678");

        when(repository.findById(existingId)).thenReturn(Optional.of(oldInstitution));
        when(repository.save(ArgumentMatchers.any(Institution.class))).thenAnswer(i -> i.getArgument(0));

        //ACT
        var result = service.update(existingId, upInstitutionDTO);

        //ASSERT
        then(repository).should().save(institutionCaptor.capture());
        Institution updatedInstitution = institutionCaptor.getValue();

        Assertions.assertEquals("Tech Institute", updatedInstitution.getName());
        Assertions.assertEquals(oldInstitution.getId(), updatedInstitution.getId());
        Assertions.assertEquals("(11)91234-5678", updatedInstitution.getPhone());
    }

    @Test
    @DisplayName("An exception should be thrown when attempting to update a non-existent ID")
    void verificationErrorInsitutionUpdate() {

        //ARRANGE
        Long nonExistentId = 10l;
        this.upInstitutionDTO = new UpdateInstitutionDTO("Tech Institute", "(11)91234-5678");

        when(repository.findById(nonExistentId)).thenReturn(Optional.empty());

        //ASSERT + ACT
        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.update(nonExistentId, upInstitutionDTO));
        //ensuring that the save method should never be called
        then(repository).should(Mockito.never()).save(any());
    }

    @Test
    void verificationSuccessInstitutionDelete() {

        //ARRANGE
        Long existingId = 1l;
        Institution institution = new Institution();
        institution.setId(existingId);

        when(repository.findById(existingId)).thenReturn(Optional.of(institution));

        //ACT
        service.delete(existingId);

        //ASSERT
        then(repository).should().findById(existingId);
        then(repository).should().delete(institutionCaptor.capture());
        Assertions.assertEquals(existingId, institutionCaptor.getValue().getId());

    }

    @Test
    void verificationErrorInstitutionDelete() {

        //ARRANGE
        Long nonExistentId = 10l;

        when(repository.findById(nonExistentId)).thenReturn(Optional.empty());

        //ACT

        //ASSERT
        //checking if the exception was called
        then(repository).should(Mockito.never()).delete(any());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.delete(nonExistentId));
    }
}
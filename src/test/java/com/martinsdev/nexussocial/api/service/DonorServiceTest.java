package com.martinsdev.nexussocial.api.service;

import com.martinsdev.nexussocial.api.dto.InsertDonorDTO;
import com.martinsdev.nexussocial.api.dto.UpdateDonorDTO;
import com.martinsdev.nexussocial.api.exception.ResourceNotFoundException;
import com.martinsdev.nexussocial.api.exception.ValidationException;
import com.martinsdev.nexussocial.api.model.Donor;
import com.martinsdev.nexussocial.api.repository.DonorRepository;
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
class DonorServiceTest {

    @InjectMocks
    private DonorService service;

    @Mock
    private DonorRepository repository;

    private InsertDonorDTO inDonorDTO;
    private UpdateDonorDTO upDonorDTO;

    @Captor
    private ArgumentCaptor<Donor> donorCaptor;

    @Test
    @DisplayName("It should save the Donor when registering")
    void verificationSucessDonorRegistration() {

        //ARRANGE
        Long existingId = 1l;
        this.inDonorDTO = new InsertDonorDTO("Michael Smith", "(31)98765-4321", "michael.smith@email.com");
        Donor donorMockado = new Donor(inDonorDTO);
        donorMockado.setId(existingId);

        when(repository.existsByNameOrEmail(anyString(), anyString())).thenReturn(false);
        when(repository.save(ArgumentMatchers.any(Donor.class))).thenReturn(donorMockado);

        //ACT
        var result = service.insert(inDonorDTO);

        //ASSERT
        then(repository).should().save(donorCaptor.capture());
        Donor savedDonor = donorCaptor.getValue();

        Assertions.assertEquals(existingId, result.id());
        Assertions.assertEquals(donorMockado.getEmail(), savedDonor.getEmail());
    }

    @Test
    @DisplayName("It should not save the Institution when registering. Ex: Donor already Exists")
    void verificationErrorDonorRegistration() {

        //ARRANGE
        this.inDonorDTO = new InsertDonorDTO("Michael Smith", "(31)98765-4321", "michael.smith@email.com");

        when(repository.existsByNameOrEmail(anyString(), anyString())).thenReturn(true);

        //ASSERT + ACT
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> service.insert(inDonorDTO));
        then(repository).should(Mockito.never()).save(any());
        //comparing the message that is inside the exception
        Assertions.assertEquals("This donor already exists", exception.getMessage());
    }

    @Test
    @DisplayName("It should save the Donor when update")
    void verificationSucessDonorUpdate() {

        //ARRANGE
        Long existingId = 1l;
        Donor oldDonor = new Donor();
        oldDonor.setId(existingId);
        oldDonor.setName("Michael Smith");
        oldDonor.setPhone("(31)98765-4321");
        oldDonor.setEmail("michael.smith@email.com");

        this.upDonorDTO = new UpdateDonorDTO("Emily Johnson", "11987654321", "emily.johnson@email.com");

        when(repository.findById(existingId)).thenReturn(Optional.of(oldDonor));
        when(repository.save(ArgumentMatchers.any(Donor.class))).thenAnswer(i -> i.getArgument(0));

        //ACT
        service.update(existingId, upDonorDTO);

        //ASSERT
        then(repository).should().save(donorCaptor.capture());
        Donor updatedDonor = donorCaptor.getValue();

        Assertions.assertEquals("Emily Johnson", updatedDonor.getName());
        Assertions.assertEquals(oldDonor.getId(), updatedDonor.getId());
    }

    @Test
    @DisplayName("An exception should be thrown when attempting to update a non-existent ID")
    void verificationErrorDonorUpdate() {

        //ARRANGE
        Long nonExistentId = 10l;
        this.upDonorDTO = new UpdateDonorDTO("David Brown", "(41)91234-9876", "david.brown@email.com");

        when(repository.findById(nonExistentId)).thenReturn(Optional.empty());

        //ASSERT + ACT
        then(repository).should(Mockito.never()).save(any(Donor.class));
        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.update(nonExistentId, upDonorDTO));
    }

    @Test
    void verificationSucessDonorDelete() {

        //ARRANGE
        Long existingId = 1l;
        Donor donor = new Donor();
        donor.setId(existingId);

        when(repository.findById(existingId)).thenReturn(Optional.of(donor));

        //ACT
        service.delete(existingId);

        //ASSERT
        then(repository).should().findById(existingId);
        then(repository).should().delete(donorCaptor.capture());
        Assertions.assertEquals(existingId, donorCaptor.getValue().getId());
    }

    @Test
    void verificationErrorDonorDelete() {

        //ARRANGE
        Long nonExistentId = 10l;

        when(repository.findById(nonExistentId)).thenReturn(Optional.empty());

        //ASSERT + ACT
        then(repository).should(Mockito.never()).delete(any(Donor.class));
        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.delete(nonExistentId));
    }
}
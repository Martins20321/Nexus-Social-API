package com.martinsdev.nexussocial.api.service;

import com.martinsdev.nexussocial.api.dto.InsertNecessityDTO;
import com.martinsdev.nexussocial.api.dto.UpdateNecessityDTO;
import com.martinsdev.nexussocial.api.exception.ResourceNotFoundException;
import com.martinsdev.nexussocial.api.exception.ValidationException;
import com.martinsdev.nexussocial.api.model.Address;
import com.martinsdev.nexussocial.api.model.Institution;
import com.martinsdev.nexussocial.api.model.Necessity;
import com.martinsdev.nexussocial.api.model.enums.NecessityStatus;
import com.martinsdev.nexussocial.api.model.enums.UrgencyLevel;
import com.martinsdev.nexussocial.api.repository.InstitutionRepository;
import com.martinsdev.nexussocial.api.repository.NecessityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NecessityServiceTest {

    @InjectMocks
    private NecessityService service;

    @Mock
    private NecessityRepository repository;

    @Mock
    private InstitutionRepository institutionRepository;

    private InsertNecessityDTO inNecessityDTO;
    private UpdateNecessityDTO upNecessityDTO;

    @Captor
    private ArgumentCaptor<Necessity> necessityCaptor;

    @Test
    @DisplayName("It should save the Necessity when registering")
    void verificationSucessNecessityRegistration() {

        //ARRANGE
        Long existingInstitutionId = 1l;
        Long existingNecessityId = 1l;
        Address addressMockado = new Address(2l, "Maple Street", "123A", "Downtown", "New York", "NY");

        Institution institutionMockado = new Institution(existingInstitutionId, "ONG RN", "12.345.678/0001-95", "(81)98321-5423", "ongrn@email.com", addressMockado, null);

        this.inNecessityDTO = new InsertNecessityDTO(institutionMockado.getId(), "Food Supplies", "Non-perishable food items for donation",
                100, UrgencyLevel.MEDIUM, NecessityStatus.OPEN);
        Necessity necessityMockado = new Necessity(inNecessityDTO, institutionMockado);
        necessityMockado.setId(existingNecessityId);

        when(repository.save(ArgumentMatchers.any(Necessity.class))).thenReturn(necessityMockado);
        when(institutionRepository.findById(existingInstitutionId)).thenReturn(Optional.of(institutionMockado));

        //ACT
        var result = service.insert(inNecessityDTO);

        //ASSERT
        then(repository).should().save(necessityCaptor.capture());
        Necessity savedNecessity = necessityCaptor.getValue();

        Assertions.assertEquals("Food Supplies", savedNecessity.getTitle());
        Assertions.assertEquals(necessityMockado.getId(), result.id());
    }

    @Test
    @DisplayName("It should not save the Necessity when registering. Ex: Necessity already exists")
    void verificationErrorNecessityAlreadyExists() {

        //ARRANGE
        Long existingInstitutionId = 1l;
        Address addressMockado = new Address(2l, "Maple Street", "123A", "Downtown", "New York", "NY");

        Institution institutionMockado = new Institution(existingInstitutionId, "ONG RN", "12.345.678/0001-95", "(81)98321-5423", "ongrn@email.com", addressMockado, null);

        this.inNecessityDTO = new InsertNecessityDTO(institutionMockado.getId(), "Food Supplies", "Non-perishable food items for donation",
                100, UrgencyLevel.MEDIUM, NecessityStatus.OPEN);

        when(institutionRepository.findById(existingInstitutionId)).thenReturn(Optional.of(institutionMockado));
        when(repository.existsByTitle(anyString())).thenReturn(true);

        //ASSERT + ACT
        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> service.insert(inNecessityDTO));
        Assertions.assertEquals("This necessity already exists", exception.getMessage());
        then(repository).should(Mockito.never()).save(any(Necessity.class));
    }

    @Test
    @DisplayName("It should save the Necessity when update")
    void verificationSucessNecessityUpdate() {

        //ARRANGE
        Long existingId = 1l;
        Long existingInstitutionId = 3l;

        Address addressMockado = new Address(2l, "Maple Street", "123A", "Downtown", "New York", "NY");
        Institution institutionMockado = new Institution(existingInstitutionId, "ONG RN", "12.345.678/0001-95", "(81)98321-5423", "ongrn@email.com", addressMockado, null);

        Necessity necessityMockado = new Necessity(existingId, "Food Supplies", "Non-perishable food items for donation",
                100, 0, LocalDateTime.now(), UrgencyLevel.MEDIUM, NecessityStatus.OPEN, institutionMockado, null);

        this.upNecessityDTO = new UpdateNecessityDTO("Winter Clothes", "Jackets and blankets for cold weather", 50, 40);

        when(repository.findById(existingId)).thenReturn(Optional.of(necessityMockado));
        when(repository.save(ArgumentMatchers.any(Necessity.class))).thenAnswer(i -> i.getArgument(0));
        //ACT
        var result = service.update(existingId, upNecessityDTO);

        //ASSERT
        then(repository).should().save(necessityCaptor.capture());
        Necessity updateNecessity = necessityCaptor.getValue();

        Assertions.assertEquals(necessityMockado.getId(), result.id());
        Assertions.assertEquals("Winter Clothes", updateNecessity.getTitle());
    }

    @Test
    @DisplayName("An exception should be thrown when attempting to update a non-existent ID")
    void verificationErorNecessityUpdate() {

        //ARRANGE
        Long nonExistentId = 1l;
        this.upNecessityDTO = new UpdateNecessityDTO("Winter Clothes", "Jackets and blankets for cold weather", 50, 40);

        when(repository.findById(nonExistentId)).thenReturn(Optional.empty());

        //ASSERT + ACT
        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.update(nonExistentId, upNecessityDTO));
        //ensuring that the save method should never be called
        then(repository).should(Mockito.never()).save(any());
    }

    @Test
    void verificationSucessNecessityDelete() {

        //ARRANGE
        Long existingId = 1l;
        Necessity necessity = new Necessity();
        necessity.setId(existingId);

        when(repository.findById(existingId)).thenReturn(Optional.of(necessity));

        //ACT
        service.delete(existingId);

        //ASSERT
        then(repository).should().findById(existingId);
        then(repository).should().delete(necessityCaptor.capture());
        Assertions.assertEquals(existingId, necessityCaptor.getValue().getId());

    }

    @Test
    void verificationErrorNecessityDelete() {

        //ARRANGE
        Long nonExistentId = 10l;

        when(repository.findById(nonExistentId)).thenReturn(Optional.empty());


        //ASSERT + ACT
        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.delete(nonExistentId));
        then(repository).should().findById(nonExistentId);
        then(repository).should(Mockito.never()).delete(any());
    }
}
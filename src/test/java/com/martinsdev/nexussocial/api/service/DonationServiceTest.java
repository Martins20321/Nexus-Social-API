package com.martinsdev.nexussocial.api.service;

import com.martinsdev.nexussocial.api.dto.InsertDonationDTO;
import com.martinsdev.nexussocial.api.dto.UpdateDonationDTO;
import com.martinsdev.nexussocial.api.exception.ResourceNotFoundException;
import com.martinsdev.nexussocial.api.model.*;
import com.martinsdev.nexussocial.api.model.enums.NecessityStatus;
import com.martinsdev.nexussocial.api.model.enums.UrgencyLevel;
import com.martinsdev.nexussocial.api.repository.DonationRepository;
import com.martinsdev.nexussocial.api.repository.DonorRepository;
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
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DonationServiceTest {

    @InjectMocks
    private DonationService service;

    @Mock
    private DonationRepository repository;

    @Mock
    private DonorRepository donorRepository;

    @Mock
    private NecessityRepository necessityRepository;

    private InsertDonationDTO inDonationDTO;
    private UpdateDonationDTO upDonationDTO;

    @Captor
    private ArgumentCaptor<Donation> donationCaptor;

    @Test
    @DisplayName("Should successfully register a donation and update necessity status to FULFILLED")
    void verificationSucessDonationRegistration() {

        //ARRANGE
        Long existingDonationId = 1l;
        Long existingNecessityId = 2l;
        Long existingDonorId = 3l;
        Long existingInstitutionId = 4l;

        Address addressMockado = new Address(2l, "Maple Street", "123A", "Downtown", "New York", "NY");

        Institution institutionMockado = new Institution(existingInstitutionId, "ONG RN", "12.345.678/0001-95", "(81)98321-5423", "ongrn@email.com", addressMockado, null);

        Necessity necessityMockado = new Necessity(existingNecessityId, "Food Supplies", "Non-perishable food items for donation",
                100, 0, LocalDateTime.now(), UrgencyLevel.MEDIUM, NecessityStatus.OPEN, institutionMockado, null);

        Donor donorMockado = new Donor(existingDonorId, "Alex Green", "(61)98321-0932", "alex@email.com", null);

        this.inDonationDTO = new InsertDonationDTO(existingNecessityId, existingDonorId, 100);

        when(donorRepository.findById(existingDonorId)).thenReturn(Optional.of(donorMockado));
        when(necessityRepository.findById(existingNecessityId)).thenReturn(Optional.of(necessityMockado));

        when(repository.save(any(Donation.class))).thenAnswer(i -> {
            Donation donationMockado = i.getArgument(0);
            donationMockado.setId(existingDonationId);
            return donationMockado;
        });

        //ACT
        var result = service.insert(inDonationDTO);

        //ASSERT
        then(repository).should().save(donationCaptor.capture());
        Assertions.assertEquals(existingDonationId, result.id());
        //testing the rule's functionality
        Assertions.assertEquals(NecessityStatus.FULFILLED, necessityMockado.getNecessityStatus());
    }

    @Test
    @DisplayName("It should not save the Donation when registering. Ex: Necessity not found")
    void verificationErrorDonationRegistration() {

        //ARRANGE
        Long nonExistentNecessityId = 10l;
        Long existingDonorId = 1l;

        Donor donorMockado = new Donor(existingDonorId, "Alex Green", "(61)98321-0932", "alex@email.com", null);

        this.inDonationDTO = new InsertDonationDTO(nonExistentNecessityId, existingDonorId, 100);

        when(donorRepository.findById(existingDonorId)).thenReturn(Optional.of(donorMockado));
        when(necessityRepository.findById(nonExistentNecessityId)).thenReturn(Optional.empty());

        //ASSERT + ACT
        var exception = Assertions.assertThrows(ResourceNotFoundException.class, () -> service.insert(inDonationDTO));
        Assertions.assertTrue(exception.getMessage().contains(nonExistentNecessityId.toString()));

        then(necessityRepository).should().findById(nonExistentNecessityId);
        then(repository).should(Mockito.never()).save(any());
    }

    @Test
    @DisplayName("It should save the Donation when update")
    void verificationSucessDonationUpdate() {

        //ARRANGE
        Long existingDonationId = 1l;
        Long existingNecessityId = 2l;
        Long existingDonorId = 3l;
        Long existingInstitutionId = 4l;

        Address addressMockado = new Address(2l, "Maple Street", "123A", "Downtown", "New York", "NY");

        Institution institutionMockado = new Institution(existingInstitutionId, "ONG RN", "12.345.678/0001-95", "(81)98321-5423", "ongrn@email.com", addressMockado, null);

        Necessity necessityMockado = new Necessity(existingNecessityId, "Winter Clothes", "Jackets and blankets for cold weather",
                50, 3, LocalDateTime.now(), UrgencyLevel.HIGH, NecessityStatus.OPEN, institutionMockado, null);

        Donor donorMockado = new Donor(existingDonorId, "Alex Green", "(61)98321-0932", "alex@email.com", null);

        Donation oldDonation = new Donation(existingDonationId, 3, LocalDateTime.now(), necessityMockado, donorMockado);

        this.upDonationDTO = new UpdateDonationDTO(50);

        when(repository.findById(existingDonationId)).thenReturn(Optional.of(oldDonation));

        //ACT
        var result = service.update(existingDonationId, upDonationDTO);

        //ASSERT
        then(repository).should().save(donationCaptor.capture());

        Assertions.assertEquals(existingDonationId, result.id());
        Assertions.assertEquals(NecessityStatus.FULFILLED, necessityMockado.getNecessityStatus());
    }

    @Test
    @DisplayName("An exception should be thrown when attempting to update a non-existent ID")
    void verificationErrorDonationUpdate() {

        //ARRANGE
        Long nonExistentDonationId = 11l;
        this.upDonationDTO = new UpdateDonationDTO(100);

        when(repository.findById(nonExistentDonationId)).thenReturn(Optional.empty());

        //ASSERT + ACT
        var exception = Assertions.assertThrows(ResourceNotFoundException.class, () -> service.update(nonExistentDonationId, upDonationDTO));
        Assertions.assertTrue(exception.getMessage().contains(nonExistentDonationId.toString()));

        then(repository).should(Mockito.never()).save(any());
    }

    @Test
    void verificationSucessNecessityDelete() {

        //ARRANGE
        Long existingDonationId = 1l;
        Long existingNecessityId = 2l;
        Long existingDonorId = 3l;
        Long existingInstitutionId = 4l;

        Address addressMockado = new Address(2l, "Maple Street", "123A", "Downtown", "New York", "NY");

        Institution institutionMockado = new Institution(existingInstitutionId, "ONG RN", "12.345.678/0001-95", "(81)98321-5423", "ongrn@email.com", addressMockado, null);

        Necessity necessityMockado = new Necessity(existingNecessityId, "Winter Clothes", "Jackets and blankets for cold weather",
                50, 32, LocalDateTime.now(), UrgencyLevel.HIGH, NecessityStatus.OPEN, institutionMockado, null);

        Donor donorMockado = new Donor(existingDonorId, "Alex Green", "(61)98321-0932", "alex@email.com", null);

        Donation donation = new Donation(existingDonationId, 32, LocalDateTime.now(), necessityMockado, donorMockado);

        when(repository.findById(existingDonationId)).thenReturn(Optional.of(donation));

        //ACT
        service.delete(existingDonationId);

        //ASSERT
        then(repository).should().delete(donationCaptor.capture());
        then(repository).should().findById(existingDonationId);

        Assertions.assertEquals(0, necessityMockado.getReachedQuantity());
        Assertions.assertEquals(NecessityStatus.OPEN, necessityMockado.getNecessityStatus());
    }

    @Test
    void verificationErrorNecessityDelete() {

        //ARRANGE
        Long nonExistentId = 10l;

        when(repository.findById(nonExistentId)).thenReturn(Optional.empty());

        //ASSERT + ACT
        ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class, () -> service.delete(nonExistentId));
        Assertions.assertTrue(exception.getMessage().contains(nonExistentId.toString()));
        then(repository).should(Mockito.never()).delete(any());
    }
}
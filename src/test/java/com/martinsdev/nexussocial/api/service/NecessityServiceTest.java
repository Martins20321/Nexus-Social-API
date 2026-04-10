package com.martinsdev.nexussocial.api.service;

import com.martinsdev.nexussocial.api.dto.InsertInstitutionDTO;
import com.martinsdev.nexussocial.api.dto.InsertNecessityDTO;
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

import java.util.Optional;

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

}
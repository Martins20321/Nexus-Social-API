package com.martinsdev.nexussocial.api.controller;

import com.martinsdev.nexussocial.api.dto.InsertDonationDTO;
import com.martinsdev.nexussocial.api.dto.UpdateDonationDTO;
import com.martinsdev.nexussocial.api.model.*;
import com.martinsdev.nexussocial.api.model.enums.NecessityStatus;
import com.martinsdev.nexussocial.api.model.enums.UrgencyLevel;
import com.martinsdev.nexussocial.api.repository.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ActiveProfiles("test")
class DonationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DonationRepository repository;

    @Autowired
    private NecessityRepository necessityRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private InstitutionRepository institutionRepository;

    @Autowired
    private DonorRepository donorRepository;

    private Long existingId;
    private Long nonexistentId = 10l;
    private Long existingNecessityId;
    private Long existingAddressId;
    private Long existingInstitutionId;
    private Long existingDonorId;

    @Autowired
    private JacksonTester<InsertDonationDTO> insertDonationDTOJacksonTester;

    @Autowired
    private JacksonTester<UpdateDonationDTO> updateDonationDTOJacksonTester;

    @BeforeEach
    void initialization() {
        repository.deleteAll();
        donorRepository.deleteAll();
        necessityRepository.deleteAll();
        institutionRepository.deleteAll();
        addressRepository.deleteAll();

        Address address = new Address(null, "street", "20", "string", "Brasília", "DF");
        Address savedAddress = addressRepository.save(address);
        this.existingAddressId = savedAddress.getId();

        Institution institution = new Institution(null, "ONG RN", "12.345.678/0001-95", "(81)98321-5423", "ongrn@email.com", savedAddress, null);
        Institution savedInstitution = institutionRepository.save(institution);
        this.existingInstitutionId = savedInstitution.getId();

        Necessity necessity = new Necessity(null, "Basic Food Baskets", "Need baskets for families in need",
                200, 100, LocalDateTime.now(),
                UrgencyLevel.MEDIUM, NecessityStatus.OPEN, savedInstitution, null);
        Necessity savedNecessity = necessityRepository.save(necessity);
        this.existingNecessityId = savedNecessity.getId();

        Donor donor = new Donor(null, "Pedro Roberto", "(61) 99231-0932", "pedro@email.com", null);
        Donor savedDonor = donorRepository.save(donor);
        this.existingDonorId = savedDonor.getId();

        Donation donation = new Donation(null, 50, LocalDateTime.now(), savedNecessity, savedDonor);
        Donation savedDonation = repository.save(donation);
        this.existingId = savedDonation.getId();
    }

    @Test
    @DisplayName("It should return code 200 (Sucess) when performing the get request all Donations")
    void verificationOfCode200WhenListAll() throws Exception {

        //ARRANGE

        //ACT
        var response = mockMvc.perform(
                get("/donations")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(200, response.getStatus());
        String content = response.getContentAsString();
        Assertions.assertFalse(content.isEmpty());
    }

    @Test
    @DisplayName("It should return code 200 (Sucess) when performing the get request findById")
    void verificationOfCode200WhenFindById() throws Exception {

        //ARRANGE

        //ACT
        var response = mockMvc.perform(
                get("/donations/{id}", existingId)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    @DisplayName("It should return code 404 (Not Found) when performing the get request findById")
    void verificationOfCode404WhenFindById() throws Exception {

        //ARRANGE

        //ACT
        var response = mockMvc.perform(
                get("/donations/{id}", nonexistentId)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(404, response.getStatus());
    }

    @Test
    @DisplayName("It should return code 201 (Created) when register Donation")
    void verificationOfSucessWhenRegisteringDonation() throws Exception {

        //ARRANGE
        InsertDonationDTO donationDTO = new InsertDonationDTO(existingNecessityId, existingDonorId, 100);

        //ACT
        var response = mockMvc.perform(
                post("/donations")
                        .content(insertDonationDTOJacksonTester.write(donationDTO).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(201, response.getStatus());
    }

    @Test
    @DisplayName("It should return code 404 (Not Found) when register Donation. Ex: Invalid NecessityId")
    void verificationOfErrorNotFoundWhenRegisteringDonation() throws Exception {

        //ARRANGE
        InsertDonationDTO donationDTO = new InsertDonationDTO(10l, existingDonorId, 100);

        //ACT
        var response = mockMvc.perform(
                post("/donations")
                        .content(insertDonationDTOJacksonTester.write(donationDTO).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(404, response.getStatus());
    }

    @Test
    @DisplayName("It should return code 200 (Sucess) when update Donation.")
    void verificationOfSucessWhenUpdateDonation() throws Exception {

        //ARRANGE
        UpdateDonationDTO donationDTO = new UpdateDonationDTO(150);

        //ACT
        var response = mockMvc.perform(
                put("/donations/{id}", existingId)
                        .content(updateDonationDTOJacksonTester.write(donationDTO).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    @DisplayName("It should return code 404 (Not Found) when update Donation. Ex: Resource Not Found")
    void verificationOfErrorWhenUpdateDonation() throws Exception {

        //ARRANGE
        UpdateDonationDTO donationDTO = new UpdateDonationDTO(150);

        //ACT
        var response = mockMvc.perform(
                put("/donations/{id}", nonexistentId)
                        .content(updateDonationDTOJacksonTester.write(donationDTO).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(404, response.getStatus());
    }

    @Test
    @DisplayName("It should return code 204 (No Content) when delete Donation. ")
    void verificationOfSucessWhenDeleteDonation() throws Exception {

        //ARRANGE

        //ACT
        var response = mockMvc.perform(
                delete("/donations/{id}", existingId)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(204, response.getStatus());
    }
}
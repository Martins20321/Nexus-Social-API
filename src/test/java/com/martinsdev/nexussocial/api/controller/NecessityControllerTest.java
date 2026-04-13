package com.martinsdev.nexussocial.api.controller;

import com.martinsdev.nexussocial.api.dto.InsertNecessityDTO;
import com.martinsdev.nexussocial.api.dto.UpdateNecessityDTO;
import com.martinsdev.nexussocial.api.model.Address;
import com.martinsdev.nexussocial.api.model.Institution;
import com.martinsdev.nexussocial.api.model.Necessity;
import com.martinsdev.nexussocial.api.model.enums.NecessityStatus;
import com.martinsdev.nexussocial.api.model.enums.UrgencyLevel;
import com.martinsdev.nexussocial.api.repository.AddressRepository;
import com.martinsdev.nexussocial.api.repository.InstitutionRepository;
import com.martinsdev.nexussocial.api.repository.NecessityRepository;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class NecessityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NecessityRepository repository;

    @Autowired
    private InstitutionRepository institutionRepository;

    @Autowired
    private AddressRepository addressRepository;

    private Long existingId;
    private Long existingInstitutionId;
    private Long existingAddressId;
    private Long noneexistentId = 10l;

    private Necessity savedNecessity;

    @Autowired
    private JacksonTester<InsertNecessityDTO> insertNecessityDTOJacksonTester;

    @Autowired
    private JacksonTester<UpdateNecessityDTO> updateNecessityDTOJacksonTester;

    @BeforeEach
    void initialization() {
        repository.deleteAll();
        institutionRepository.deleteAll();
        addressRepository.deleteAll();

        Address address = new Address(null, "street", "20", "string", "Brasília", "DF");
        Address savedAddress = addressRepository.save(address);
        this.existingAddressId = savedAddress.getId();

        Institution institution = new Institution(null, "ONG RN", "12.345.678/0001-95",
                "(81)98321-5423", "ongrn@email.com", savedAddress, null);
        Institution savedInstitution = institutionRepository.save(institution);
        this.existingInstitutionId = savedInstitution.getId();

        Necessity necessity = new Necessity(null, "Basic Food Baskets", "Need baskets for families in need",
                200, 100, LocalDateTime.now(),
                UrgencyLevel.MEDIUM, NecessityStatus.OPEN, savedInstitution, null);
        this.savedNecessity = repository.save(necessity);
        this.existingId = savedNecessity.getId();
    }

    @Test
    @DisplayName("It should return code 200 (Sucess) when performing the get request all necessities")
    void verificationOfCode200WhenListAll() throws Exception {

        //ARRANGE

        //ACT
        var response = mockMvc.perform(get("/necessities").contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();

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
                get("/necessities/{id}", existingId)
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
                get("/necessities/{id}", noneexistentId)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(404, response.getStatus());
    }

    @Test
    @DisplayName("It should return code 201 (Created) when register Necessity")
    void verificationOfSucessWhenRegisteringNecessity() throws Exception {

        //ARRANGE
        InsertNecessityDTO necessityDTO = new InsertNecessityDTO(existingInstitutionId, "Winter Blankets",
                "Collection of blankets for the homeless population due to the upcoming cold front",
                300, UrgencyLevel.HIGH, NecessityStatus.OPEN);

        //ACT
        var response = mockMvc.perform(
                post("/necessities")
                        .content(insertNecessityDTOJacksonTester.write(necessityDTO).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(201, response.getStatus());
    }

    @Test
    @DisplayName("It should return code 400 (Bad Request) when register Necessity. Ex: Title not given")
    void verificationOfErrorWhenRegisteringNecessity() throws Exception {

        //ARRANGE
        InsertNecessityDTO necessityDTO = new InsertNecessityDTO(existingInstitutionId, null, "Description example",
                30, UrgencyLevel.LOW, NecessityStatus.IN_PROGRESS);

        //ACT
        var response = mockMvc.perform(
                post("/necessities")
                        .content(insertNecessityDTOJacksonTester.write(necessityDTO).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(400, response.getStatus());
    }

    @Test
    @DisplayName("It should return code 200 (Sucess) when update Necessity")
    void verificationOfSucessWhenUpdateNecessity() throws Exception {

        //ARRANGE
        UpdateNecessityDTO necessityDTO = new UpdateNecessityDTO(null, null, null, 180);

        //ACT
        var response = mockMvc.perform(
                put("/necessities/{id}", existingId)
                        .content(updateNecessityDTOJacksonTester.write(necessityDTO).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(200, response.getStatus());

        Necessity updateNecessity = repository.findById(existingId).get();
        Assertions.assertEquals(180, updateNecessity.getReachedQuantity());
    }

    @Test
    @DisplayName("It should return code 404 (Not Found) when update Necessity")
    void verificationOfErrorWhenUpdateNecessity() throws Exception {

        //ARRANGE
        UpdateNecessityDTO necessityDTO = new UpdateNecessityDTO(null, null, null, 400);

        //ACT
        var response = mockMvc.perform(
                put("/necessities/{id}", noneexistentId)
                        .content(updateNecessityDTOJacksonTester.write(necessityDTO).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(404, response.getStatus());
    }

    @Test
    @DisplayName("It should return code 204 (No Content) when Delete Necessity")
    void verificationOfSucessWhenDeleteNecessity() throws Exception {

        //ARRANGE

        //ACT
        var response = mockMvc.perform(
                delete("/necessities/{id}", existingId)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(204, response.getStatus());
    }
}
package com.martinsdev.nexussocial.api.controller;

import com.martinsdev.nexussocial.api.dto.InsertDonorDTO;
import com.martinsdev.nexussocial.api.dto.UpdateDonorDTO;
import com.martinsdev.nexussocial.api.model.Donor;
import com.martinsdev.nexussocial.api.repository.DonorRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class DonorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DonorRepository repository;

    private Long existingId;
    private Long nonexistentId = 10l;

    @Autowired
    private JacksonTester<InsertDonorDTO> insertDonorDTOJacksonTester;

    @Autowired
    private JacksonTester<UpdateDonorDTO> updateDonorDTOJacksonTester;

    @BeforeEach
    void initialization(){
        repository.deleteAll();
        Donor donor = new Donor(null, "Pedro Roberto", "(61) 99231-0932", "pedro@email.com", null);
        Donor savedDonor = repository.save(donor);
        this.existingId = savedDonor.getId();
    }

    @Test
    @DisplayName("It should return code 200 (Sucess) when performing the get request all Donors")
    void verificationOfCode200WhenListAll() throws Exception {

        //ARRANGE

        //ACT
        var response = mockMvc.perform(
                get("/donors")
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
                get("/donors/{id}", existingId)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    @DisplayName("It should return code 404 (Not Found) when performing the get request findById")
    void verificationOfCode404WhenFindById() throws Exception {

        //ARRANGE

        //ACT
        var response = mockMvc.perform(
                get("/donors/{id}", nonexistentId)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(404, response.getStatus());
    }

    @Test
    @DisplayName("It should return code 201 (Created) when register Donor")
    void verificationOfSucessWhenRegisteringDonor() throws Exception {

        //ARRANGE
        InsertDonorDTO donorDTO = new InsertDonorDTO("Rogério Marcos","(84)99453-0123", "roberto@email.com");

        //ACT
        var response = mockMvc.perform(
                post("/donors")
                        .content(insertDonorDTOJacksonTester.write(donorDTO).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(201, response.getStatus());
    }

    @Test
    @DisplayName("It should return code 400 (Bad Request) when register Donor. Ex: Null Values")
    void verificationOfErrorWhenRegisteringDonor() throws Exception {

        //ARRANGE
        InsertDonorDTO donorDTO = new InsertDonorDTO(null, null, null);

        //ACT
        var response = mockMvc.perform(
                post("/donors")
                        .content(insertDonorDTOJacksonTester.write(donorDTO).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(400, response.getStatus());
    }

    @Test
    @DisplayName("It should return code 200 (Sucess) when update Donor.")
    void verificationOfSucessWhenUpdateDonor() throws Exception {

        //ARRANGE
        UpdateDonorDTO donorDTO = new UpdateDonorDTO("Rogério Marcos", "(84)99453-0123", "roberto@email.com");

        //ACT
        var response = mockMvc.perform(
                put("/donors/{id}", existingId)
                        .content(updateDonorDTOJacksonTester.write(donorDTO).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    @DisplayName("It should return code 404 (Not Found) when update Donor. ")
    void verificationOfErrorWhenUpdateDonor() throws Exception {

        //ARRANGE
        UpdateDonorDTO donorDTO = new UpdateDonorDTO("Rogério Marcos", "(84)99453-0123", "roberto@email.com");
        //ACT
        var response = mockMvc.perform(
                put("/donors/{id}", nonexistentId)
                        .content(updateDonorDTOJacksonTester.write(donorDTO).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(404, response.getStatus());
    }

    @Test
    @DisplayName("It should return code 204 (No Content) when delete Donor. ")
    void verificationOfSucessWhenDeleteDonor() throws Exception {

        //ARRANGE

        //ACT
        var response = mockMvc.perform(
                delete("/donors/{id}", existingId)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(204, response.getStatus());
    }
}
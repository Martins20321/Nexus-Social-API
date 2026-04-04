package com.martinsdev.nexussocial.api.controller;

import com.martinsdev.nexussocial.api.dto.InsertInstitutionDTO;
import com.martinsdev.nexussocial.api.dto.UpdateInstitutionDTO;
import com.martinsdev.nexussocial.api.model.Address;
import com.martinsdev.nexussocial.api.model.Institution;
import com.martinsdev.nexussocial.api.repository.AddressRepository;
import com.martinsdev.nexussocial.api.repository.InstitutionRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ActiveProfiles("test")
class InstitutionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InstitutionRepository repository;

    @Autowired
    private AddressRepository addressRepository;

    private Long existingId;
    private Long nonexistentId = 10l;
    private Long addressId;

    @Autowired
    private JacksonTester<InsertInstitutionDTO> insertInstitutionDTOJacksonTester;

    @Autowired
    private JacksonTester<UpdateInstitutionDTO> updateInstitutionDTOJacksonTester;

    @BeforeEach
    void initialization(){
        repository.deleteAll();
        addressRepository.deleteAll();

        Address address = new Address(null, "street", "20", "string", "Brasília", "DF");
        Address savedAddress = addressRepository.save(address);
        this.addressId = savedAddress.getId();

        Institution institution = new Institution(null, "ONG RN", "12.345.678/0001-95", "(81)98321-5423","ongrn@email.com", savedAddress, null);
        Institution savedInstitution = repository.save(institution);
        this.existingId = savedInstitution.getId();
    }

    @Test
    @DisplayName("It should return code 200 (Sucess) when performing the get request all institutions")
    void verificationOfCode200WhenListAll() throws Exception {

        //ARRANGE

        //ACT
        var response = mockMvc.perform(
                get("/institutions")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(200, response.getStatus());
        String content = response.getContentAsString();
        Assertions.assertFalse(content.isEmpty());
    }

    @Test
    @DisplayName("It should return code 200 (Sucess) when performing the get request findById")
    void verificationOfSucessWhenFindById() throws Exception {

        //ARRANGE

        //ACT
        var response = mockMvc.perform(
                get("/institutions/{id}", existingId)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    @DisplayName("It should return code 404 (Not Found) when performing the get request findById")
    void verificationOfErrorWhenFindById() throws Exception {

        //ARRANGE

        //ACT
        var response = mockMvc.perform(
                get("/institutions/{id}", nonexistentId)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(404, response.getStatus());
    }

    @Test
    @DisplayName("It should return code 201 (Created) when register Institution")
    void verificationOfSucessWhenRegisterInstitution() throws Exception {

        //ARRANGE
        Address address = new Address(null, "street3", "98", "string90", "São Paulo", "SP");
        Address savedAddress = addressRepository.save(address);
        Long addressId2 = savedAddress.getId();

        InsertInstitutionDTO institutionDTO = new InsertInstitutionDTO(
                "Learn More", "00.000.000/0001-00", "(61)99362-6213",
                "learnmore@email.com", addressId2);

        //ACT
        var response = mockMvc.perform(
                post("/institutions")
                        .content(insertInstitutionDTOJacksonTester.write(institutionDTO).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(201, response.getStatus());
    }

    @Test
    @DisplayName("It should return code 400 (Bad Request) when register Institution. Ex: Institution alread exists")
    void verificationOfErrorWhenRegisterInstitution() throws Exception {

        //ARRANGE
        //instantiating with the same data from @BeforeEach
        InsertInstitutionDTO institutionDTO = new InsertInstitutionDTO("ONG RN", "12.345.678/0001-95", "(81)98321-5423","ongrn@email.com", addressId);

        //ACT
        var response = mockMvc.perform(
                post("/institutions")
                        .content(insertInstitutionDTOJacksonTester.write(institutionDTO).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(400, response.getStatus());
    }

    @Test
    @DisplayName("It should return code 200 (Sucess) when update Institution.")
    void verificationOfSucessWhenUpdateInstitution() throws Exception {

        //ARRANGE
        UpdateInstitutionDTO institutionDTO = new UpdateInstitutionDTO("ONG EcoAção Brasi", "(11)96750-4324");

        //ACT
        var response = mockMvc.perform(
                put("/institutions/{id}", existingId)
                        .content(updateInstitutionDTOJacksonTester.write(institutionDTO).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    @DisplayName("It should return code 404 (Not Found) when update Institution.")
    void verificationOfErrorWhenUpdateInstitution() throws Exception {

        //ARRANGE
        UpdateInstitutionDTO institutionDTO = new UpdateInstitutionDTO("ONG EcoAção Brasi", "(11)96750-4324");

        //ACT
        var response = mockMvc.perform(
                put("/institutions/{id}", nonexistentId)
                        .content(updateInstitutionDTOJacksonTester.write(institutionDTO).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(404, response.getStatus());
    }

    @Test
    @DisplayName("It should return code 204 (NoContent) when delete Institution.")
    void verificationOfSucessWhenDeleteInstitution() throws Exception {

        //ARRANGE

        //ACT
        var response = mockMvc.perform(
            delete("/institutions/{id}", existingId)
                    .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(204, response.getStatus());
    }
}
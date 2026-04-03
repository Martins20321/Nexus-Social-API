package com.martinsdev.nexussocial.api.controller;

import com.martinsdev.nexussocial.api.dto.InsertAddressDTO;
import com.martinsdev.nexussocial.api.dto.UpdateAddressDTO;
import com.martinsdev.nexussocial.api.model.Address;
import com.martinsdev.nexussocial.api.repository.AddressRepository;
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
class AddressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AddressRepository repository;

    private Long existingId;
    private Long nonexistentId = 10l;

    @Autowired
    private JacksonTester<InsertAddressDTO> addressDTOJacksonTester;

    @Autowired
    private JacksonTester<UpdateAddressDTO> updateAddressDTOJacksonTester;

    @BeforeEach
    void initialization() {
        repository.deleteAll();
        Address address = new Address(null, "street", "20", "string", "Brasília", "DF");
        Address savedAddress = repository.save(address);
        this.existingId = savedAddress.getId();
    }

    @Test
    @DisplayName("It should return code 200 (Sucess) when performing the get request all addresses")
    void verificationOfCode200WhenListAll() throws Exception {

        //ARRANGE

        //ACT
        var response = mockMvc.perform(
                get("/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(200, response.getStatus());

        String content = response.getContentAsString();
        Assertions.assertFalse(content.isEmpty());
    }

    @Test
    @DisplayName("It should return code 200 (Sucess) when performing the get request findById")
    void verficationOfCode200WhenFindById() throws Exception {

        //ARRANGE

        //ACT
        var response = mockMvc.perform(
                get("/addresses/{id}", existingId)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    @DisplayName("It should return code 404 (Not Found) when performing the get request findById")
    void verficationOfCode404WhenFindById() throws Exception{

        //ARRANGE

        //ACT
        var response = mockMvc.perform(
                get("/addresses/{id}", nonexistentId)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(404, response.getStatus());
    }

    @Test
    @DisplayName("It should return code 201 (Create) when register address")
    void verificationOfSuccessWhenRegisteringAddress() throws Exception {

        //ARRANGE
        InsertAddressDTO addressDTO = new InsertAddressDTO("street", "20", "string", "Brasília", "DF");

        //ACT
        var response = mockMvc.perform(
                post("/addresses")
                        .content(addressDTOJacksonTester.write(addressDTO).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        //ASSERTIVE
        Assertions.assertEquals(201, response.getStatus());
    }

    @Test
    @DisplayName("It should return code 400 (Bad Request) when register address. Ex: Null Values")
    void verificationOfErrorWhenRegisteringAddress() throws Exception {

        //ARRANGE
        InsertAddressDTO addressDTO = new InsertAddressDTO(null, null, null, null, null);

        //ACT
        var response = mockMvc.perform(
                post("/addresses")
                        .content(addressDTOJacksonTester.write(addressDTO).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andReturn().getResponse();

        //ASSERTIVE
        Assertions.assertEquals(400, response.getStatus());
    }

    @Test
    @DisplayName("It should return code 200 (Sucess) when update address.")
    void verificationOfSucessWhenUpdateAddress() throws Exception {

        //ARRANGE
        UpdateAddressDTO addressDTO = new UpdateAddressDTO("street", "20", "string", "Brasília", "DF");

        //ACT
        var response = mockMvc.perform(
                put("/addresses/{id}", existingId)
                        .content(updateAddressDTOJacksonTester.write(addressDTO).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        //ASSERTIVE
        Assertions.assertEquals(200, response.getStatus());
    }

    @Test
    @DisplayName("It should return code 404 (Not Found) when update address.")
    void verificationOfErrorWhenUpdateAddress() throws Exception {

        //ARRANGE
        UpdateAddressDTO addressDTO = new UpdateAddressDTO("street", "20", "string", "Brasília", "DF");

        //ACT
        var response = mockMvc.perform(
                put("/addresses/{id}", nonexistentId)
                        .content(updateAddressDTOJacksonTester.write(addressDTO).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(404, response.getStatus());
    }

    @Test
    @DisplayName("It should return code 204 (NoContent) when delete address.")
    void verificationOfSucessWhenDeleteAddress() throws Exception {

        //ARRANGE

        //ACT
        var response = mockMvc.perform(
                delete("/addresses/{id}", existingId)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(204, response.getStatus());
    }
}
package com.martinsdev.nexussocial.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.martinsdev.nexussocial.api.dto.DataAuthenticationDTO;
import com.martinsdev.nexussocial.api.infra.security.DataRefreshTokenDTO;
import com.martinsdev.nexussocial.api.infra.security.TokenDataJWT;
import com.martinsdev.nexussocial.api.infra.security.TokenService;
import com.martinsdev.nexussocial.api.model.refreshtoken.RefreshToken;
import com.martinsdev.nexussocial.api.model.refreshtoken.RefreshTokenRepository;
import com.martinsdev.nexussocial.api.model.user.User;
import com.martinsdev.nexussocial.api.model.user.UserRepository;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@ActiveProfiles("test")
class AuthenticationControllerTest {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RefreshTokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JacksonTester<DataAuthenticationDTO> dataAuthenticationDTOJacksonTester;

    @Autowired
    private JacksonTester<DataRefreshTokenDTO> dataRefreshTokenDTOJacksonTester;

    @BeforeEach
    void initializaton(){
        tokenRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User(null, "maria@email.com", passwordEncoder.encode("maria123"));
        userRepository.save(user);
    }

    @Test
    @DisplayName("The system should return code 200 upon successful login")
    void verificationOfCode200WhenLogin() throws Exception {

        //ARRANGE
        DataAuthenticationDTO dataAuthenticationDTO = new DataAuthenticationDTO("maria@email.com", "maria123");

        //ACT
        var response = mockMvc.perform(
                post("/auth/login")
                        .content(dataAuthenticationDTOJacksonTester.write(dataAuthenticationDTO).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(200, response.getStatus());
    }


    @Test
    @DisplayName("The system should return code 403 when attempting to log in without an authenticated user")
    void verificationOfCode403WhenLogin() throws Exception {

        //ARRANGE
        DataAuthenticationDTO dataAuthenticationDTO = new DataAuthenticationDTO("maria@email.com", "maria123456788");

        //ACT
        var response = mockMvc.perform(
                post("/auth/login")
                        .content(dataAuthenticationDTOJacksonTester.write(dataAuthenticationDTO).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(403, response.getStatus());
    }

    @Test
    @DisplayName("The system should return code 200 upon successful token refresh")
    void verificationOfCode200WhenRefreshToken() throws Exception {

        //ARRANGE
        DataAuthenticationDTO dataAuthenticationDTO = new DataAuthenticationDTO("maria@email.com", "maria123");

        //ACT
        var response = mockMvc.perform(
                post("/auth/login")
                        .content(dataAuthenticationDTOJacksonTester.write(dataAuthenticationDTO).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andReturn().getResponse();

        String responseContent = response.getContentAsString();

        //deserializes to the TokenDataJWT record
        TokenDataJWT tokenData = new ObjectMapper().readValue(responseContent, TokenDataJWT.class);
        //Acess to refreshToken
        String refreshToken = tokenData.refreshToken();

        var response2 = mockMvc.perform(
                post("/auth/refresh")
                        .content(dataRefreshTokenDTOJacksonTester.write(new DataRefreshTokenDTO(refreshToken)).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(200, response2.getStatus());
    }

    @Test
    @DisplayName("The system should return code 401 when refresh token is used")
    void verificationOfCode401WhenRefreshTokenIsUsed() throws Exception {

        //ARRANGE
        DataAuthenticationDTO dataAuthenticationDTO = new DataAuthenticationDTO("maria@email.com", "maria123");

        //ACT
        var response = mockMvc.perform(
                post("/auth/login")
                        .content(dataAuthenticationDTOJacksonTester.write(dataAuthenticationDTO).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andReturn().getResponse();

        String responseContent = response.getContentAsString();

        //deserializes to the TokenDataJWT record
        TokenDataJWT tokenData = new ObjectMapper().readValue(responseContent, TokenDataJWT.class);
        //Acess to refreshToken
        String refreshToken = tokenData.refreshToken();

        mockMvc.perform(
                post("/auth/refresh")
                        .content(dataRefreshTokenDTOJacksonTester.write(new DataRefreshTokenDTO(refreshToken)).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andReturn().getResponse();

        var response2 = mockMvc.perform(
                post("/auth/refresh")
                        .content(dataRefreshTokenDTOJacksonTester.write(new DataRefreshTokenDTO(refreshToken)).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print()).andReturn().getResponse();

        //ASSERT
        Assertions.assertEquals(401, response2.getStatus());
    }
}
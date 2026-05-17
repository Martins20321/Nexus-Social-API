package com.martinsdev.nexussocial.api.infra.client;

import com.martinsdev.nexussocial.api.dto.InsertAddressDTO;
import com.martinsdev.nexussocial.api.dto.ViaCepResponseDTO;
import com.martinsdev.nexussocial.api.infra.exception.InvalidZipCodeException;
import com.martinsdev.nexussocial.api.service.AddressService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class ViaCepClientTest {

    @Autowired
    private AddressService addressService;

    @MockitoBean
    private ViaCepClient cepClient;

    @Test
    @DisplayName("The system should automatically return the address information with a valid zip code")
    void verificationZipCodeValid() {

        //ARRANGE
        InsertAddressDTO dto = new InsertAddressDTO("01001000", "2");
        ViaCepResponseDTO responseDTOValid = new ViaCepResponseDTO("01001-000", "Praça da Sé", "Sé", "São Paulo", "SP", false);

        when(cepClient.findAddressByZipCode("01001000")).thenReturn(responseDTOValid);

        //ACT
        var result = addressService.insert(dto);

        //ASSERT
        Assertions.assertEquals("01001-000", result.zipCode());
    }

    @Test
    @DisplayName("The system should return an error if an invalid ZIP code(11 digits) is entered.")
    void verificationZipCodeInvalid() {

        //ARRANGE
        InsertAddressDTO dto = new InsertAddressDTO("12345678901", "91");

        when(cepClient.findAddressByZipCode(anyString())).thenThrow(new InvalidZipCodeException(dto.zipCode()));

        //ASSERT + ACT
        Assertions.assertThrows(InvalidZipCodeException.class, () -> addressService.insert(dto));
    }
}
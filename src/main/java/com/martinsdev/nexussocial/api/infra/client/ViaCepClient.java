package com.martinsdev.nexussocial.api.infra.client;

import com.martinsdev.nexussocial.api.dto.ViaCepResponseDTO;
import com.martinsdev.nexussocial.api.infra.exception.InvalidZipCodeException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Component
public class ViaCepClient {

    private final RestClient restClient;

    public ViaCepClient() {
        this.restClient = RestClient.create("https://viacep.com.br/ws");
    }

    public ViaCepResponseDTO findAddressByZipCode(String zipCode) {
        try {
            ViaCepResponseDTO response = restClient.get()
                    .uri("/{zipCode}/json", zipCode)
                    .retrieve()
                    .body(ViaCepResponseDTO.class);

            if (response.erro()) {
                throw new InvalidZipCodeException(zipCode);
            }

            return response;
        }
        catch (HttpClientErrorException ex){
            throw new InvalidZipCodeException(zipCode);
        }
    }
}

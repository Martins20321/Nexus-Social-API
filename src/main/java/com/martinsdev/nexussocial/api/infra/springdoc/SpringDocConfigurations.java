package com.martinsdev.nexussocial.api.infra.springdoc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfigurations {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
                .info(new Info()
                        .title("Nexus Social API")
                        .description("This is a secure RESTful API developed to centralize " +
                                "and optimize the donation logistics for small NGOs and community centers. " +
                                "The system acts as a bridge, allowing local institutions to register their critical needs while providing donors with a real-time, " +
                                "priority-based view of where their contributions can make the most impact.")
                        .contact(new Contact()
                                .name("José Gabriel Martins")
                                .email("jgmsilva11@gmail.com")
                                .url("https://github.com/Martins20321"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}

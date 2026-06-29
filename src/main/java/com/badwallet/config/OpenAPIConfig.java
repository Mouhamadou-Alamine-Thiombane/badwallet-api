package com.badwallet.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI badWalletOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("BadWallet API")
                .description("API de portefeuille électronique - Examen Design Pattern")
                .version("1.0.0"));
    }
}

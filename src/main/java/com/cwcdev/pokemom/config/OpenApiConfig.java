// config/OpenApiConfig.java
package com.cwcdev.pokemom.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${app.environment:local}")
    private String environment;

    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8080");
        devServer.setDescription("Servidor de Desenvolvimento");

        Server productionServer = new Server();
        productionServer.setUrl("https://www.cwc3d.net");
        productionServer.setDescription("Servidor de Produção");

        Contact contact = new Contact();
        contact.setEmail("calebewerneck@hotmail.com");
        contact.setName("CWCDEV");
        contact.setUrl("https://www.cwc3d.net");

        License mitLicense = new License()
                .name("MIT License")
                .url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("Pokémon API")
                .version("1.0.0")
                .contact(contact)
                .description("API para gerenciamento e cache de dados de Pokémon")
                .termsOfService("https://www.seudominio.com/terms")
                .license(mitLicense);

        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer, productionServer));
    }
}
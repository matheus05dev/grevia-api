package com.projeto1cc.grevia.core.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
                .info(new Info()
                        .title("Grevia API 🌱")
                        .description("""
                                **Grevia** é uma plataforma inteligente de recomendação e gestão de plantas, \
                                pensada para aproximar pessoas da natureza com tecnologia e praticidade.
                                
                                Com a Grevia API você pode:
                                - 🌿 Gerenciar usuários e autenticação segura com JWT
                                - 🪴 Cadastrar e consultar espécies de plantas
                                - 💧 Criar e acompanhar planos de cuidado personalizados
                                - 📊 Receber recomendações baseadas no perfil de cada plantio
                                
                                Desenvolvida com **Spring Boot 3**, **MySQL** e deploy contínuo via **Railway**.
                                """)
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Time Grevia")
                                .email("contactgrevia@gmail.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")));
    }
}

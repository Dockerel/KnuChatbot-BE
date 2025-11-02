package knu_chatbot.application.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    public static final String BEARER_AUTH = "BearerAuth";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(
                        new Components()
                                .addSecuritySchemes(BEARER_AUTH, createSecurityScheme())
                )
                .info(createApiInfo())
                .addSecurityItem(
                        new SecurityRequirement().addList(BEARER_AUTH)
                );
    }

    private Info createApiInfo() {
        return new Info()
                .title("KNU CSE ChatBot 명세서")
                .version("0.0.1");
    }

    private SecurityScheme createSecurityScheme() {
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");
        return securityScheme;
    }
}
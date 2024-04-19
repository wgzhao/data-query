package com.github.wgzhao.dbquery.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "springdoc.api-docs.enabled", havingValue = "true", matchIfMissing = true)
public class SpringdocConfig
{
    private final License license = new License().name("Apache 2.0").url("https://github.com/wgzhao/data-query");

    private SecurityScheme securityScheme()
    {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT");
    }

    @Bean
    public OpenApiCustomizer adminApiCustomizer()
    {
        return openApi -> openApi.addSecurityItem(
                new SecurityRequirement()
                        .addList("Authorization"))
                .components(new Components().addSecuritySchemes("Authorization", securityScheme()));
    }

    @Bean
    public OpenApiCustomiser openApiCustomiser()
    {
        return openApi -> openApi.addSecurityItem(
                new SecurityRequirement()
                        .addList("Authorization"))
                .components(new Components().addSecuritySchemes("Authorization", securityScheme()));
    }

    @Bean
    public GroupedOpenApi adminOpenApi()
    {
        return GroupedOpenApi.builder().group("admin").pathsToMatch("/admin/**")
                .addOpenApiCustomizer(adminApiCustomizer())
                .build();
    }

    @Bean
    public GroupedOpenApi queryOpenApi()
    {
        return GroupedOpenApi.builder().group("query").pathsToMatch("/api/v1/**").build();
    }

    public OpenAPI queryOpenAPI()
    {
        return new OpenAPI().info(new Info().title("DB Query API").version("1.0").license(license));
    }
}

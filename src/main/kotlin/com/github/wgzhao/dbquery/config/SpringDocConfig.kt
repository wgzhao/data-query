package com.github.wgzhao.dbquery.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springdoc.core.customizers.OpenApiCustomiser
import org.springdoc.core.customizers.OpenApiCustomizer
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnProperty(name = ["springdoc.api-docs.enabled"], havingValue = "true", matchIfMissing = true)
open class SpringDocConfig {
    private val license: License? = License().name("Apache 2.0").url("https://github.com/wgzhao/data-query")

    private fun securityScheme(): SecurityScheme? {
        return SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
    }

    @Bean
    open fun adminApiCustomizer(): OpenApiCustomizer {
        return OpenApiCustomizer { openApi: OpenAPI? ->
            openApi!!.addSecurityItem(
                SecurityRequirement()
                    .addList("Authorization")
            )
                .components(Components().addSecuritySchemes("Authorization", securityScheme()))
        }
    }

    @Bean
    open fun openApiCustomiser(): OpenApiCustomiser {
        return OpenApiCustomiser { openApi: OpenAPI? ->
            openApi!!.addSecurityItem(
                SecurityRequirement()
                    .addList("Authorization")
            )
                .components(Components().addSecuritySchemes("Authorization", securityScheme()))
        }
    }

    @Bean
    open fun adminOpenApi(): GroupedOpenApi? {
        return GroupedOpenApi.builder().group("admin").pathsToMatch("/admin/**")
            .addOpenApiCustomizer(adminApiCustomizer())
            .build()
    }

    @Bean
    open fun queryOpenApi(): GroupedOpenApi? {
        return GroupedOpenApi.builder().group("query").pathsToMatch("/api/v1/**").build()
    }

    fun queryOpenAPI(): OpenAPI? {
        return OpenAPI().info(Info().title("DB Query API").version("1.0").license(license))
    }
}

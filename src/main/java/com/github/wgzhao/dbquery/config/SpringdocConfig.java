package com.github.wgzhao.dbquery.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.swing.GroupLayout;

@Component
public class SpringdocConfig
{
    @Bean
    public GroupedOpenApi adminOpenApi()
    {
        return GroupedOpenApi.builder().group("admin").pathsToMatch("/admin/**").build();
    }

    @Bean
    GroupedOpenApi queryOpenApi()
    {
        return GroupedOpenApi.builder().group("query").pathsToMatch("/api/v1/**").build();
    }
}

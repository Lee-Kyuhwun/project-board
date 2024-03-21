package com.fastcampus.projectboard;

import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcBuilderCustomizer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;

@TestConfiguration
public class RestDocsConfiguration {

    @Bean
    public MockMvcBuilderCustomizer restDocumentationConfigurer(RestDocumentationContextProvider restDocumentation) {
        return builder -> builder.apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation));
    }
}

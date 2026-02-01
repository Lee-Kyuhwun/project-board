package com.fastcampus.projectboard.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;

@Configuration
public class DataRestConfig {
    @Bean
    public RepositoryRestConfigurer repositoryRestConfigurer() {
        return RepositoryRestConfigurer.withConfig(config -> { // RepositoryRestConfigurer 설정
            config.exposeIdsFor( // 엔티티의 ID를 REST API에 노출
                    com.fastcampus.projectboard.domain.UserAccount.class // UserAccount 엔티티의 ID 노출
            );
        });
    }
}

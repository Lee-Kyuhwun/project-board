package com.fastcampus.projectboard.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;


@EnableJpaAuditing // JPA Auditing 활성화
@Configuration
public class JpaConfig {


    // AudirtorAware란  JPA Auditing 기능을 사용하기 위해선, AuditorAware 인터페이스를 구현한 클래스가 필요하다.
    // AuditorAware는 현재 사용자가 누구인지를 파악하는 인터페이스이다.
    // 즉, Spring Security를 사용한다면 SecurityContext에서 사용자 정보를 꺼내오는 기능을 구현하면 된다.
    @Bean
    public AuditorAware<String> auditorAware(){
        return () -> Optional.of("uno"); // TODO: 스프링 시큐리티로 인증 기능을 붙이게 될 때, 수정해야한다.
    }


}

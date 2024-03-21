package com.fastcampus.projectboard.controller;

import com.fastcampus.projectboard.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({SecurityConfig.class})
@WebMvcTest(MainController.class)
class MainControllerTest {

    private final MockMvc mvc;

    MainControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }


    @Test
    void giveNothing_whenRequestingRootPage_thenRedirectToArticlesPage() throws Exception {
        // given

        // when
        mvc.perform(get("/")).
                andExpect(status().is3xxRedirection())
                .andDo(document("root-redirect")); // Spring REST Docs 문서화 부분
        // .andExpect(redirectedUrl("/articles"));
        // then
    }

}
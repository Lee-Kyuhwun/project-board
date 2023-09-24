package com.fastcampus.projectboard.controller;


import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Disabled("Spring Data REST 통합테스트는 불필요하므로 제외시킴")
@DisplayName("Data REST - API 테스트")
@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class DataRestTest {

    private final MockMvc mvc;

    DataRestTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }


    @DisplayName("[api] 게시글 리스트 조회")
    @Test
    void givenNothing_whenRequestingArticles_thenReturnsArticlesJsonResponse() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/api/articles"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json")));
    }

    @DisplayName("[api] 게시글 단건 조회")
    @Test
    void givenNothing_whenRequestingArticle_thenReturnsArticleJsonResponse() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/api/articles/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json")));
    }

    @DisplayName("[api] 게시글 -> 댓글 리스트 조회")
    @Test
    void givenNothing_whenRequestingArticleCommentsFromArticle_thenReturnsArticleCommentsJsonResponse() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/api/articles/1/articleComments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json")));
    }

    @DisplayName("[api] 댓글 리스트 조회")
    @Test
    void givenNothing_whenRequestingArticleComments_thenReturnsArticleCommentsJsonResponse() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/api/articleComments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json")));
    }

    @DisplayName("[api] 댓글 단건 조회")
    @Test
    void givenNothing_whenRequestingArticleComment_thenReturnsArticleCommentJsonResponse() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/api/articleComments/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json")));
    }

    @DisplayName("[api] 회원 관련 API 는 일체 제공하지 않는다.")
    @Test
    void givenNothing_whenRequestingUserAccounts_thenThrowsException() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/api/userAccounts")).andExpect(status().isNotFound());
        mvc.perform(post("/api/userAccounts")).andExpect(status().isNotFound());
        mvc.perform(put("/api/userAccounts")).andExpect(status().isNotFound());
        mvc.perform(patch("/api/userAccounts")).andExpect(status().isNotFound());
        mvc.perform(delete("/api/userAccounts")).andExpect(status().isNotFound());
        mvc.perform(head("/api/userAccounts")).andExpect(status().isNotFound());
    }

}
/*
@Disabled("Spring Data REST 통합테스트는 불필요하므로 제외시킴 ")
// 스프링 부트 테스트 환경에서 MockMvc를 자동으로 설정합니다.
@AutoConfigureMockMvc
// 스프링 부트의 테스트를 위한 어노테이션으로, 애플리케이션 컨텍스트를 로드하여 테스트를 진행합니다.
@SpringBootTest
// JUnit5를 사용할 때 테스트 설명을 위해 사용하는 어노테이션입니다.
@DisplayName("DataRest - API 테스트")
@Transactional
public class DataRestTest {


    // MockMvc는 웹 서버를 실행하지 않고도 스프링 MVC (Controller 계층)를 테스트할 수 있게 해주는 클래스입니다.
    private final MockMvc mvc;

    // 생성자 주입을 통해 MockMvc 인스턴스를 받아옵니다.
    public DataRestTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }



    @DisplayName("[api] 게시글 리스트 조회")
    @Test
    void givenNothing_whenRequestingArticles_thenReturnsArticlesJsonResponse() throws Exception {
        // given: 테스트 데이터 준비 및 초기 설정

        // when: 테스트할 기능을 수행
        mvc.perform(get("/api/articles"))
                .andExpect(status().isOk()) // HTTP 상태 코드가 200 (OK) 인지 검사
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json"))); // 반환된 컨텐츠 타입이 'application/hal+json'인지 검사
        // then: 결과 검증 (위에서 이미 검증하였기 때문에 별도로 작성하지 않음)
    }

    @DisplayName("[api] 게시글 단건 조회")
    @Test
    @Transactional  // 해당 어노테이션은 테스트 동작 중 발생한 DB의 변화를 테스트 종료 후 롤백합니다. 즉, 테스트에 의한 영향을 DB에 남기지 않게 합니다.
    void givenNothing_whenRequestingArticle_thenReturnsArticleJsonResponse() throws Exception {
        // given
        // MockMvc를 사용하여 게시글 생성 API를 호출합니다.
        // contentType은 전송하는 데이터의 타입을 지정하며, 여기서는 JSON 형식입니다.
        // content는 전송할 실제 데이터를 지정합니다.
        MvcResult result = mvc.perform(post("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"제목\",\"content\":\"내용\",\"hashtag\":\"해시태그\"}"))
                .andExpect(status().isCreated())  // 응답 상태가 '생성됨'(201)인지 확인합니다.
                .andReturn();  // 테스트의 결과를 MvcResult 객체에 저장하여 후속 처리나 검증에 사용합니다.

        // MvcResult는 MockMvc의 실행 결과를 나타내는 객체로, 응답의 상태, 데이터, 헤더 등의 정보를 포함합니다.
        String location = result.getResponse().getHeader("Location");  // 생성된 게시글의 URI를 Location 헤더에서 가져옵니다.
        String articleId = location.substring(location.lastIndexOf('/') + 1);  // Location URI에서 게시글의 ID를 추출합니다.

        // when
        // 추출한 게시글 ID를 사용하여 해당 게시글을 조회하는 API를 호출합니다.
        mvc.perform(get("/api/articles/" + articleId))
                .andExpect(status().isOk())  // 응답 상태가 '정상'(200)인지 확인합니다.
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json")));  // 응답의 컨텐츠 타입이 'application/hal+json'인지 확인합니다.
        // then
        // 이 부분에는 결과에 대한 추가적인 검증 로직이 들어갈 수 있습니다.
    }




    @DisplayName("[api] 게시글 -> 댓글 리스트 조회")
    @Test
    @Transactional
    void givenNothing_whenRequestingArticleCommentsFromArticle_thenReturnsArticleCommentsJsonResponse() throws Exception {
        // given
        // 게시글을 생성합니다.
        MvcResult articleResult = mvc.perform(post("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"제목\",\"content\":\"내용\",\"hashtag\":\"해시태그\"}"))
                .andExpect(status().isCreated())
                .andReturn();

        String articleLocation = articleResult.getResponse().getHeader("Location");

        // 게시글에 대한 댓글을 생성합니다.
        mvc.perform(post("/api/articleComments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"article\":\"" + articleLocation + "\",\"content\":\"댓글내용\"}"))
                .andExpect(status().isCreated());

        // when
        // 해당 게시글의 댓글 목록을 조회합니다.
        String articleId = articleLocation.substring(articleLocation.lastIndexOf('/') + 1);
        mvc.perform(get("/api/articles/" + articleId + "/articleComments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json")));
        // then: 결과를 검증하는 부분입니다 (위에서 이미 검증하였기 때문에 별도로 작성하지 않음).
    }


    @DisplayName("[api] 댓글 리스트 조회")
    @Test
    void givenNothing_whenRequestingArticleComments_thenReturnsArticleCommentsJsonResponse() throws Exception {
        // given
        //예시데이터 삽입



        // when
        mvc.perform(get("/api/articleComments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json")));
        // then

    }


    @DisplayName("[api] 댓글 단건 조회")
    @Test
    @Transactional
    void givenNothing_whenRequestingArticleComment_thenReturnsArticleCommentJsonResponse() throws Exception {
        // given
        //예시데이터 삽입
        mvc.perform(post("/api/articles")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"제목\",\"content\":\"내용\",\"hashtag\":\"해시태그\"}"))
                .andExpect(status().isCreated());
        mvc.perform(post("/api/articleComments")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"article\":\"http://localhost/api/articles/1\",\"content\":\"댓글내용\"}"))
                .andExpect(status().isCreated());

        // when
        mvc.perform(get("/api/articleComments/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json")));
        // then
    }
}
*/

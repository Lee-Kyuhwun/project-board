package com.fastcampus.projectboard.controller;

import com.fastcampus.projectboard.config.JpaConfig;
import com.fastcampus.projectboard.config.SecurityConfig;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@Disabled("구현 중")
@Import({SecurityConfig.class})
@DisplayName("View Controller - 게시글")
@WebMvcTest(ArticleController.class) // 입력한 컨트롤러만 테스트하겠다는 의미
class ArticleControllerTest {
    private final MockMvc mvc;

    ArticleControllerTest(@Autowired MockMvc mvc) { // 테스트에서는 꼭 @Autowired를 붙여줘야 한다.
        this.mvc = mvc;
    }


    @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticleView_thenReturnArticlesView() throws Exception {
        // given

        // when

        // then
        mvc.perform(get("/articles"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                // 이 뷰는 데이터가 있어야한다.
                // 테이블에 게시글 목록이 있을텐데 그것은 서버에서 데이터 목록을 보여줬다는 이야기이고
                // 그 뜻은 modelAttribute로 데이터를 넘겨줬다는 이야기이다.
                // 그러므로 modelAttribute로 넘겨준 데이터가 있는지 확인해야한다.
                .andExpect(view().name("articles/index")) // viewName이 index인지 확인해야한다.
                .andExpect(model().attributeExists("articles")); // modelAttribute로 넘겨준 데이터가 있는지 확인해야한다.
    }
    @DisplayName("[view][GET] 게시글 상세 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticleView_thenReturnArticleView() throws Exception {
        // given
        mvc.perform(MockMvcRequestBuilders.post("/articles")
                .param("title", "제목")
                .param("content", "내용")
                .param("createdBy", "작성자"));
        // when

        // then
        mvc.perform(get("/articles/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/detail"))
                .andExpect(model().attributeExists("articles")) // modelAttribute로 넘겨준 데이터가 있는지 확인해야한다.
                .andExpect(model().attributeExists("articleComments")); // modelAttribute로 넘겨준 데이터가 있는지 확인해야한다.

    }

    @DisplayName("[view][GET] 게시글 검색 전용 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticleSearchView_thenReturnArticleView() throws Exception {
        // given

        // when

        // then
        mvc.perform(get("/articles/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().attributeExists("articles/search"));
                // 이 뷰는 데이터가 있어야한다.
                // 테이블에 게시글 목록이 있을텐데 그것은 서버에서 데이터 목록을 보여줬다는 이야기이고
                // 그 뜻은 modelAttribute로 데이터를 넘겨줬다는 이야기이다.
                // 그러므로 modelAttribute로 넘겨준 데이터가 있는지 확인해야한다.

        }
        @Disabled("구현 중")
    @DisplayName("[view][GET] 게시글 해시태그 검색 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingHashtagSearchView_thenReturnArticleHashTagSearchView() throws Exception {
        // given

        // when

        // then
        mvc.perform(get("/articles/search-hashtag"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().attributeExists("articles/search-hashtag"));
        // 이 뷰는 데이터가 있어야한다.
        // 테이블에 게시글 목록이 있을텐데 그것은 서버에서 데이터 목록을 보여줬다는 이야기이고
        // 그 뜻은 modelAttribute로 데이터를 넘겨줬다는 이야기이다.
        // 그러므로 modelAttribute로 넘겨준 데이터가 있는지 확인해야한다.
    }




}
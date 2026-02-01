package com.fastcampus.projectboard.controller;

import com.fastcampus.projectboard.config.SecurityConfig;
import com.fastcampus.projectboard.repository.ArticleCommentRepository;
import com.fastcampus.projectboard.repository.ArticleRepository;
import com.fastcampus.projectboard.repository.UserAccountRepository;
import com.fastcampus.projectboard.service.ArticleCommentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("View 컨트롤러 - 댓글")
@Import({SecurityConfig.class})
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(ArticleCommentController.class) // View 컨트롤러 테스트
class ArticleCommentControllerTest {

    private final MockMvc mvc;

    @MockBean // 실제 빈 대신 목 빈을 주입
    private ArticleCommentService articleCommentService;

    @Mock private ArticleRepository articleRepository;

    @Mock private ArticleCommentRepository articleCommentRepository;

    @Mock private UserAccountRepository userAccountRepository;

    public ArticleCommentControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("[view][POST] 댓글 등록 - 정상 호출")
    @Test
    void givenArticleCommentInfo_whenRequestingNewComment_thenRedirectsToArticleView() throws Exception {
        // Given
        Long articleId = 1L;

        // When & Then
        mvc.perform(post("/comments/new")
                        .param("articleId", articleId.toString())
                        .param("content", "comment"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/articles/" + articleId));
    }

    @DisplayName("[view][POST] 댓글 삭제 - 정상 호출")
    @Test
    void givenArticleCommentId_whenRequestingCommentDeletion_thenRedirectsToArticleView() throws Exception {
        // Given
        Long articleId = 1L;
        Long commentId = 2L;

        // When & Then
        mvc.perform(post("/comments/{commentId}/delete", commentId)
                        .param("articleId", articleId.toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/articles/" + articleId));
    }

}

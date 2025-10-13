package com.fastcampus.projectboard.service;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.dto.ArticleCommentDto;
import com.fastcampus.projectboard.dto.ArticleDto;
import com.fastcampus.projectboard.dto.ArticleUpdateDto;
import com.fastcampus.projectboard.repository.ArticleCommentRepository;
import com.fastcampus.projectboard.repository.ArticleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;


@ExtendWith(MockitoExtension.class)
class ArticleCommentServiceTest {

    @InjectMocks // Mock을 주입하는 대상을 표시
    private ArticleCommentService sut; // sut = system under test
    @Mock
    private ArticleCommentRepository articleCommentRepository;

    @Mock
    private  ArticleRepository articleRepository;

/*    검색
    각 게시글 페이지로 이동
    페이지네이션
    홈 버튼 -> 게시판 페이지로 리다이렉션
    정렬 기능*/
    @DisplayName("게시글Id로 댓글을 조회하면, 해당 게시글의 댓글 리스트를 반환한다.")
    @Test
    void givenArticleId_whenSearchingComments_thenReturnsArtilceComment(){
        // given
        Long articleId = 1L;


        // given
        // articleRepository에서 articleId로 게시글을 조회하는 mock 설정
        given(articleRepository.findById(articleId))
                .willReturn(Optional.of(Article.of("title", "content", "#java")));
        // when
        List<ArticleCommentDto> articleComments = sut.searchArticlesComment(articleId);

        assertThat(articleComments).isNotNull();

        // then
        // articleRepository의 findById 메서드가 articleId로 호출되었는지 검증
        then(articleRepository).should().findById(articleId);

    }

    @DisplayName("댓글 정보를 입력하면, 댓글을 생성한다.")
    @Test
    void givenArticleId_whenSearchingArticle_thenReturnsArticle(){
        // given


        // when
        Page<ArticleDto> articles = sut.searchArticles( 1L);// 제목, 본문, ID, 닉네임, 해시태그


        // then
        assertThat(Collections.singletonList(articles)).isNull();
    }

    @DisplayName("게시글 ID와 댓글 정보를 입력하면, 댓글을 생성하고 저장한다.")
    @Test
    void givenArticleIdAndCommentInfo_whenSavingComment_thenSavesComment() {
        // given
        Long articleId = 1L;
        var commentDto = ArticleCommentDto.of(
                LocalDateTime.now(),
                "writer",
                null,
                null,
                "테스트 댓글 내용"
        );

        given(articleRepository.findById(articleId))
                .willReturn(Optional.of(Article.of("title", "content", "#java")));
        given(articleCommentRepository.save(any()))
                .willReturn(null);

        // when
        sut.saveArticleComment(articleId, commentDto);

        // then
        then(articleRepository).should().findById(articleId);
        then(articleCommentRepository).should().save(any());
    }













}
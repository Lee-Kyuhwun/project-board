package com.fastcampus.projectboard.service;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.type.SearchType;
import com.fastcampus.projectboard.dto.ArticleDto;
import com.fastcampus.projectboard.dto.ArticleUpdateDto;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;


@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @InjectMocks // Mock을 주입하는 대상을 표시
    private ArticleService sut; // sut = system under test
    @Mock
    private ArticleRepository articleRepository;

/*    검색
    각 게시글 페이지로 이동
    페이지네이션
    홈 버튼 -> 게시판 페이지로 리다이렉션
    정렬 기능*/
    @DisplayName("게시글을 검색하면, 게시글 리스트를 반환한다.")
    @Test
    void givenSearchParameter_whenSearchingArticles_thenReturnsArticleList(){
        // given


        // when
        List<ArticleDto> articles = sut.searchArticles(SearchType.TITLE,"search keyword");// 제목, 본문, ID, 닉네임, 해시태그


        // then
        assertThat(articles).isNotNull();

    }

    @DisplayName("게시글을 조회하면, 게시글을 반환한다.")
    @Test
    void givenArticleId_whenSearchingArticle_thenReturnsArticle(){
        // given


        // when
        Page<ArticleDto> articles = sut.searchArticles( 1L);// 제목, 본문, ID, 닉네임, 해시태그


        // then
        assertThat(Collections.singletonList(articles)).isNull();
    }

    @DisplayName("게시글 정보를 입력하면, 게시글을 생성한다.")
    @Test
    void givenArticleInfo_whenCreatingArticle_thenCreatesArticle(){
        // given
        given(articleRepository.save(any(Article.class))).willReturn(null); // willDoNothing() 메서드는 아무것도 하지 않는다는 의미이다.

        // when
        sut.saveArticle(ArticleDto.of(LocalDateTime.now(), "Uno", "작성자", "제목", "now"));
        // then
        then(articleRepository).should().save(any(Article.class)); // Article이라면 무엇이든 상관없다.

    }


    @DisplayName("게시글 id와 수정 정보를 입력하면, 게시글을 수정한다.")
    @Test
    void givenArticleModifiedInfo_whenUpdatingArticle_thenUpdatingArticle(){
        // given
        given(articleRepository.save(any(Article.class))).willReturn(null); // willDoNothing() 메서드는 아무것도 하지 않는다는 의미이다.

        // when
        sut.updateAritcle(1L, ArticleUpdateDto.of("Uno", "작성자", "제목"));
        // then
        then(articleRepository).should().save(any(Article.class)); // Article이라면 무엇이든 상관없다.

    }



    @DisplayName("게시글 id를 입력하면  게시글을 삭제한다.")
    @Test
    void givenArticleID_whenDeletingArticle_thenDeletingArticle(){
        // given
        willDoNothing().given(articleRepository).delete(any(Article.class));

        // when
        sut.deleteArticle(1L);
        // then
        then(articleRepository).should().delete(any(Article.class)); // Article이라면 무엇이든 상관없다.

    }










}
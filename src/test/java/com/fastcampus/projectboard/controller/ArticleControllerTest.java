package com.fastcampus.projectboard.controller;

import com.fastcampus.projectboard.domain.type.SearchType;
import com.fastcampus.projectboard.dto.ArticleWithCommentsDto;
import com.fastcampus.projectboard.dto.UserAccountDto;
import com.fastcampus.projectboard.service.ArticleService;
import com.fastcampus.projectboard.service.PaginationService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@Disabled("구현 중")
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("View Controller - 게시글")
@WebMvcTest(ArticleController.class) // 입력한 컨트롤러만 테스트하겠다는 의미
class ArticleControllerTest {
    private final MockMvc mvc;
    @MockBean
    private ArticleService articleService;


    @MockBean private PaginationService paginationService;

    ArticleControllerTest(@Autowired MockMvc mvc) { // 테스트에서는 꼭 @Autowired를 붙여줘야 한다.
        this.mvc = mvc;
    }


    @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticleView_thenReturnArticlesView() throws Exception {
        // given
        given(articleService.searchArticles(eq(null), eq(null), any(Pageable.class))).willReturn(Page.empty());
        // any 쓰면 null도 허용되어서 안된다. Primitive라서 anyInt()를 써야한다.
        given(paginationService.getPaginationBarNumbers(anyInt(), anyInt())).willReturn(List.of(0,1,2,3,4));
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
                .andExpect(model().attributeExists("articles")) // modelAttribute로 넘겨준 데이터가 있는지 확인해야한다.
        .andExpect(model().attributeExists("paginationBarNumbers"));
        then(articleService).should().searchArticles(eq(null), eq(null), any(Pageable.class));
        then(paginationService).should().getPaginationBarNumbers(anyInt(), anyInt());;
    }

    @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 페이징, 정렬 기능")
    @Test
    void givenPagingAndSortingParams_whenSearchingArticlesPage_thenReturnsArticlesPage() throws Exception {
        // Given
        String sortName = "title";
        String direction = "desc";
        int pageNumber = 0;
        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Order.desc(sortName)));
        List<Integer> barNumbers = List.of(1, 2, 3, 4, 5);
        given(articleService.searchArticles(null, null, pageable)).willReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(pageable.getPageNumber(), Page.empty().getTotalPages())).willReturn(barNumbers);

        // When & Then
        mvc.perform(
                        get("/articles")
                                .queryParam("page", String.valueOf(pageNumber))
                                .queryParam("size", String.valueOf(pageSize))
                                .queryParam("sort", sortName + "," + direction)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/index"))
                .andExpect(model().attributeExists("articles"))
                .andExpect(model().attribute("paginationBarNumbers", barNumbers));
        then(articleService).should().searchArticles(null, null, pageable);
        then(paginationService).should().getPaginationBarNumbers(pageable.getPageNumber(), Page.empty().getTotalPages());
    }

    @DisplayName("[view][GET] 게시글 상세 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticleView_thenReturnArticleView() throws Exception {
        // given
        Long articleId = 1L;
        given(articleService.getArticle(articleId)).willReturn(createArticleWithCommentsDto());
        // when

        // then
        mvc.perform(get("/articles/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/detail"))
                .andExpect(model().attributeExists("article")) // modelAttribute로 넘겨준 데이터가 있는지 확인해야한다.
                .andExpect(model().attributeExists("articleComments")); // modelAttribute로 넘겨준 데이터가 있는지 확인해야한다.

        then(articleService).should().getArticle(articleId);

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

    @DisplayName("[view][GET] 게시글 검색 전용 페이지 - 정상 호출")
    @Test
    public void givenSearchKeyword_whenSearchingArticlesView_thenReturnsArticlesView() throws Exception {

        //Given
        SearchType searchType = SearchType.TITLE;
        String searchValue = "title";

        given(articleService.searchArticles(
                eq(SearchType.TITLE),
                eq(searchValue),
                any(Pageable.class)
        )).willReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(anyInt(), anyInt())).willReturn(List.of(0,1,2,3,4));

        mvc.perform(get("/articles")
                .queryParam("searchType", searchType.name())
                .queryParam("searchValue", searchValue)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/index")) // viewName이 index인지 확인해야한다.
                .andExpect(model().attributeExists("articles")) // modelAttribute로 넘겨준 데이터가 있는지 확인해야한다.
                .andExpect(model().attributeExists("paginationBarNumbers"));

        then(articleService).should().searchArticles(
                eq(SearchType.TITLE),
                eq(searchValue),
                any(Pageable.class)
        );
        then(paginationService).should().getPaginationBarNumbers(anyInt(), anyInt());


    }



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



    // 픽스쳐 코드 : 테스트를 위해 필요한 객체를 만들어 주는 메서드
    private ArticleWithCommentsDto createArticleWithCommentsDto() {

        return ArticleWithCommentsDto.of(
                1L,
                createUserAccountDto(),
                Set.of(),
                "title",
                "content",
                "#java",
                LocalDateTime.now(),
                "lkh",
                LocalDateTime.now(),
                "lkh"
        );
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
                1L,
                "lkh",
                "password",
                "test@test.com",
                "nickname",
                "memo",
                LocalDateTime.now(),
                "lkh",
                LocalDateTime.now(),
                "lkh"
        );
        }
}
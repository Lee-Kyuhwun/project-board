package com.fastcampus.projectboard.controller;


import com.fastcampus.projectboard.domain.type.SearchType;
import com.fastcampus.projectboard.dto.ArticleDto;
import com.fastcampus.projectboard.dto.UserAccountDto;
import com.fastcampus.projectboard.dto.request.ArticleRequest;
import com.fastcampus.projectboard.dto.response.ArticleResponse;
import com.fastcampus.projectboard.dto.response.ArticleWithCommentsResponse;
import com.fastcampus.projectboard.service.ArticleService;
import com.fastcampus.projectboard.service.PaginationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@Controller
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService articleService;

    private final PaginationService paginationService;

    @GetMapping
    public String articles(
            @RequestParam(required = false) SearchType searchType,
            @RequestParam(required = false) String searchValue,
           @PageableDefault(size = 10, sort = "createdAt",direction = Sort.Direction.DESC) Pageable pageable,
            ModelMap map) {
        Page<ArticleResponse> articles = articleService.searchArticles(searchType, searchValue, pageable).map(ArticleResponse::from);
        List<Integer> barNumbers = paginationService.getPaginationBarNumbers(pageable.getPageNumber(), articles.getTotalPages());
        map.addAttribute("articles",articles);
        map.addAttribute("paginationBarNumbers", barNumbers);
        map.addAttribute("searchTypes", SearchType.values()); // values()는 enum의 모든 값을 배열로 반환
        return "articles/index";
    }
    @GetMapping("/{articleid:\\d+}")
    public String detail(@PathVariable Long articleid, ModelMap map) {
        ArticleWithCommentsResponse article = ArticleWithCommentsResponse.from(
                articleService.getArticle(articleid));
        map.addAttribute("article", article);
        map.addAttribute("articleComments", article.articleCommentsResponse());
        map.addAttribute("totalCount", articleService.getArticleCount());
        return "articles/detail";
    }


    @GetMapping("/search-hashtags")
    public String searchHashtag(
            @RequestParam(required = false) String searchValue,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            ModelMap map) {
        Page<ArticleResponse> articles = articleService.searchArticlesViaHashtag(searchValue, pageable)
                .map(ArticleResponse::from);
        List<Integer> barNumbers = paginationService.getPaginationBarNumbers(pageable.getPageNumber(), articles.getTotalPages());
        List<String> hashtags = articleService.getHashtags();


        map.addAttribute("articles", articles);
        map.addAttribute("hashtags", hashtags);
        map.addAttribute("paginationBarNumbers", barNumbers);
        map.addAttribute("searchType", SearchType.HASHTAG);
        return "articles/search-hashtags";
    }


    @GetMapping("/form")
    public String articleForm(ModelMap map) {
        map.addAttribute("article", ArticleResponse.of(null, null, null, null, null, null, null));
        return "articles/form";
    }

    @PostMapping("/form")
    public String postNewArticle(ArticleRequest articleRequest) {
        // TODO: 인증 기능 구현 후 실제 사용자 정보로 대체
        UserAccountDto userAccountDto = UserAccountDto.of("uno", "pw", "uno@mail.com", "Uno", "memo");
        articleService.saveArticle(articleRequest.toDto(userAccountDto));
        return "redirect:/articles";
    }

    @GetMapping("/{articleId}/form")
    public String updateArticleForm(@PathVariable Long articleId, ModelMap map) {
        ArticleDto articleDto = articleService.getArticle(articleId).toDto();
        ArticleResponse article = ArticleResponse.from(articleDto);
        map.addAttribute("article", article);
        return "articles/form";
    }

    @PostMapping("/{articleId}/form")
    public String updateArticle(@PathVariable Long articleId, ArticleRequest articleRequest) {
        // TODO: 인증 기능 구현 후 실제 사용자 정보로 대체
        UserAccountDto userAccountDto = UserAccountDto.of("uno", "pw", "uno@mail.com", "Uno", "memo");
        articleService.updateArticle(ArticleDto.of(articleId, userAccountDto, articleRequest.title(), articleRequest.content(), articleRequest.hashtag(), null, null, null, null));
        return "redirect:/articles/" + articleId;
    }

    @PostMapping("/{articleId}/delete")
    public String deleteArticle(@PathVariable Long articleId) {
        // TODO: 인증 기능 구현 후 사용자 검증 추가
        articleService.deleteArticle(articleId);
        return "redirect:/articles";
    }

}

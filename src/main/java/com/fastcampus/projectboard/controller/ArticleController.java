package com.fastcampus.projectboard.controller;


import com.fastcampus.projectboard.domain.type.SearchType;
import com.fastcampus.projectboard.dto.response.ArticleResponse;
import com.fastcampus.projectboard.dto.response.ArticleWithCommentsResponse;
import com.fastcampus.projectboard.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RequiredArgsConstructor
@Controller
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping
    public String articles(
            @RequestParam(required = false) SearchType searchType,
            @RequestParam(required = false) String searchValue,
           @PageableDefault(size = 10, sort = "createdAt",direction = Sort.Direction.DESC) Pageable pageable,
            ModelMap map) {
        map.addAttribute("articles", articleService.searchArticles(searchType, searchValue, pageable)
                .map(ArticleResponse::from));
        return "articles/index";
    }
    @GetMapping("/{articleid}")
    public String detail(@PathVariable Long articleid, ModelMap map) {
        ArticleWithCommentsResponse article = ArticleWithCommentsResponse.from(
                articleService.getArticle(articleid));
        map.addAttribute("article", article);
        map.addAttribute("articleComments", article.articleComments());
        return "articles/detail";
    }




}

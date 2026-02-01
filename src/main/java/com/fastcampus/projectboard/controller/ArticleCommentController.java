package com.fastcampus.projectboard.controller;


import com.fastcampus.projectboard.dto.ArticleCommentRequest;
import com.fastcampus.projectboard.dto.UserAccountDto;
import com.fastcampus.projectboard.service.ArticleCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/comments")
@Controller
public class ArticleCommentController {

    private final ArticleCommentService articleCommentService;



    @PostMapping("/new")
    public String postNewArticleComment(ArticleCommentRequest articleCommentRequest) {

        // TODO: 인증 정보를 넣어줘야한다.

        articleCommentService.saveArticleComment(articleCommentRequest.toDto(UserAccountDto.of(
                "unoTest",
                "password",
                "test@test.com",
                null,
                null
        )));


        return "redirect:/articles/"+ articleCommentRequest.articleId(); // 다시 해당 게시글로 리다이렉트
    }

    @PostMapping("/{commentId}/delete")
    public String deleteArticleComment(@PathVariable Long commentId,Long articleId) {
        // 무엇을 지울지알 articleId추가
        articleCommentService.deleteArticleComment(commentId);
        return "redirect:/articles/"+ articleId;
    }
}

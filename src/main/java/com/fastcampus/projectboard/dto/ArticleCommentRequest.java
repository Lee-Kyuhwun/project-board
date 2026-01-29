package com.fastcampus.projectboard.dto;

import java.io.Serializable;

/**
 * DTO for {@link com.fastcampus.projectboard.domain.ArticleComment}
 */
public record ArticleCommentRequest(Long articleId, String content) implements Serializable {

    public static ArticleCommentRequest of(Long articleId, String content) {
        return new ArticleCommentRequest(articleId, content);
    }

    public ArticleCommentDto toDto(UserAccountDto userAccountDto) {
        return ArticleCommentDto.of(
                this.articleId,
                userAccountDto,
                this.content
        );
    }

}
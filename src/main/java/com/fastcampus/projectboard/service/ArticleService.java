package com.fastcampus.projectboard.service;


import com.fastcampus.projectboard.domain.type.SearchType;
import com.fastcampus.projectboard.dto.ArticleDto;
import com.fastcampus.projectboard.dto.ArticleUpdateDto;
import com.fastcampus.projectboard.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@RequiredArgsConstructor
@Service
@Transactional // 메소드가 실행되는 도중에 예외가 발생하면 이 메소드에서 이루어진 모든 DB작업을 rollback한다.
public class ArticleService   {

    private final ArticleRepository articleRepository;


    @Transactional(readOnly = true) // transactional을 붙이면, 이 메소드가 실행되는 도중에 예외가 발생하면 이 메소드에서 이루어진 모든 DB작업을 rollback한다.
    //readonly란 읽기전용이라는 뜻이다. 이 메소드가 실행되는 도중에 예외가 발생하면 이 메소드에서 이루어진 모든 DB작업을 rollback한다.
    public List<ArticleDto> searchArticles(SearchType title, String search_keyword) {
        return List.of(); // of()메서드란 불변의 리스트를 생성하는 메서드이다. of()메서드는 인자로 전달된 요소들을 포함하는 리스트를 생성한다.

    }
    @Transactional(readOnly = true) // transactional을 붙이면, 이 메소드가 실행되는 도중에 예외가 발생하면 이 메소드에서 이루어진 모든 DB작업을 rollback한다.
    //readonly란 읽기전용이라는 뜻이다. 이 메소드가 실행되는 도중에 예외가 발생하면 이 메소드에서 이루어진 모든 DB작업을 rollback한다.
    public Page<ArticleDto> searchArticles(long l) {
        return null;
    }

    public void saveArticle(ArticleDto dto) {
        // TODO document why this method is empty


    }

    public void updateAritcle(long articleId, ArticleUpdateDto dto) {


    }

    public void deleteArticle(long l) {

    }
}

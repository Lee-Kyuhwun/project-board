package com.fastcampus.projectboard.repository;

import com.fastcampus.projectboard.config.JpaConfig;
import com.fastcampus.projectboard.domain.Article;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import jakarta.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@Disabled
//@ActiveProfiles("testdb")
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("JPA 연결 테스트")
@Import(JpaConfig.class) // 이거 없으면 Auditing적용이 안된다. 
@DataJpaTest // 슬라이스 테스트란 테스트할 때, 필요한 것만 잘라서 테스트하는 것을 말한다. 즉, JPA 관련된 Bean들만 등록해서 테스트하겠다는 의미이다.
// @DataJpaTest 애노테이션을 사용하면, 테스트가 실행된 후에 자동으로 롤백을 수행합니다. 따라서 데이터베이스에 영구적인 변경이 발생하지 않게 됩니다.
class JpaRepositoryTest {
    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;

    public JpaRepositoryTest(@Autowired  ArticleRepository articleRepository,@Autowired ArticleCommentRepository articleCommentRepository) {
        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;
    }



    @DisplayName("select 테스트")
    @Test
    void givenTestData_whenSelecting_thenWorksFine(){
        //given
        Article savedArticle = articleRepository.save(Article.of("new article","content","uno"));
        //when
        List<Article> articles = articleRepository.findAll();
        //then
        assertThat(articles).isNotEmpty();
        assertThat(articles).hasSize(1);
    }


    @DisplayName("insert 테스트")
    @Test
    void givenTestData_whenInserting_thenWorksFine(){
        // Given
        long previousCount = articleRepository.count() ;

        // When
        Article savedArticle = articleRepository.save(Article.of("new article","content","uno"));
        // Then
        assertThat(articleRepository.count()).isEqualTo(previousCount+1);

    }

    @DisplayName("update 테스트")
    @Test
    void givenTestData_whenUpdating_thenWorksFine(){
        // Given
        Article savedArticle = articleRepository.save(Article.of("new article","content","uno"));
        String updatedHashTag = "#springboot";
        // When
        savedArticle.setHashtag(updatedHashTag);
        savedArticle.setTitle("updated title");
        Article updatedArticle = articleRepository.saveAndFlush(savedArticle);
        // Then
        assertThat(updatedArticle.getHashtag()).isEqualTo(updatedHashTag);
        assertThat(updatedArticle.getTitle()).isEqualTo("updated title");
        // 어차피 롤백할것이라 어차피 바뀌는 것이 없으므로 생략될 수 있는데 그래서 Flush를 해야지 적용된다.
    }

    @DisplayName("delete 테스트")
    @Test
    void givenTestData_whenDeleting_thenWorksFine(){
        // Given
        Article savedArticle = articleRepository.save(Article.of("new article","content","uno"));
        long previousCount = articleRepository.count();
        long previousArticleCommentCount = articleCommentRepository.count();
        // When
        articleRepository.delete(savedArticle);
        // Then
        assertThat(articleRepository.findById(savedArticle.getId())).isEmpty();
        assertThat(articleRepository.count()).isEqualTo(previousCount-1);
    }

}
package com.fastcampus.projectboard.domain;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;


@Getter
@ToString
@Table(indexes ={
        @Index(columnList = "title"),
        @Index(columnList = "hashtag"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy"),
} )
@Entity
@Builder
public class Article extends AuditingFields{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    private String title; // 제목
    @Setter
    @Column(nullable = false,length = 10000)
    private String content; // 본문

    @Setter private String hashtag; // 해시태그

    @OrderBy("id")
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    @ToString.Exclude // ArticleComment의 toString()에서 Article을 출력하지 않도록 한다.
    // ArticleComment의 toString()을 호출하면 ArticleComment의 Article 필드를 출력하게 되는데,
    // 이 때 Article의 toString()이 호출되면 ArticleComment의 toString()이 호출되고 무한루프에 빠지게 된다.
    // 따라서 ArticleComment의 toString()에서 Article을 출력하지 않도록 한다.
    // 양방향 바인딩
    private final Set<ArticleComment> articleComments = new LinkedHashSet<>();

/*
    @CreatedDate     @Column(nullable = false) private LocalDateTime createdAt; // 생성일시
    @CreatedBy    @Column(nullable = false, length = 100) private String createdBy; //생성자
    @LastModifiedDate   @Column(nullable = false) private LocalDateTime modifiedAt; // 수정일시
    @LastModifiedBy     @Column(nullable = false, length = 100) private String modifiedBy; // 수정자
*/


    // 밖에서 생성하지 못하도록 protected로 막는다.
    protected Article() {
    }

    //
    private Article(String title, String content, String hashtag) {
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }


    // 팩토리 메서드
    // of라는 이름의 팩토리 메서드를 제공한다.
    // 이 메서드는 Article 객체를 생성하고 반환하느 역할을 한다.
    // 팩토리 메서드는 static으로 선언된다.
    // static이므로 클래스의 인스턴스를 생성하지 않고도 이 메서드를 호출할 수 있다.]
    // 이 메서드를 통해 Article 객체를 생성하게 되면, 내부에서 private
    public static Article of(String title, String content ,String hashtag){
        return new Article(title, content,hashtag);
    }

    // 동등성을 검사할 수 있는


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article article)) return false;
        return id.equals(article.id);
    } // 영속성 검사를 하지않은 엔티티들은 동등성 검사에서 탈락한다.
    // 새로 만든 엔티티들이 영속화되지않으면 다른 값으로 취급한다.
    // 엔티티에 대해서 같을려면 조건이 무엇인가? 에 대한 대답

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

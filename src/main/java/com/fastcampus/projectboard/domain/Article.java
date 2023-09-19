package com.fastcampus.projectboard.domain;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
@EntityListeners(AuditingEntityListener.class) // JPA에게 이 엔티티는 Auditing 기능을 사용한다고 알린다.
// Auditing이란 엔티티의 생성일시, 생성자, 수정일시, 수정자를 자동으로 관리해주는 기능이다.
// 테스트코드에서 Auditing이 있어야지
@Entity
public class Article {

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
    @ToString.Exclude
    // 양방향 바인딩
    private final Set<ArticleComment> articleComments = new LinkedHashSet<>();

    @CreatedDate     @Column(nullable = false) private LocalDateTime createdAt; // 생성일시
    @CreatedBy    @Column(nullable = false, length = 100) private String createdBy; //생성자
    @LastModifiedDate   @Column(nullable = false) private LocalDateTime modifiedAt; // 수정일시
    @LastModifiedBy     @Column(nullable = false, length = 100) private String modifiedBy; // 수정자


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

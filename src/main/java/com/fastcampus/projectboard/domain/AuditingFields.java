package com.fastcampus.projectboard.domain;


import lombok.Generated;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;



@EntityListeners(AuditingEntityListener.class) // JPA에게 이 엔티티는 Auditing 기능을 사용한다고 알린다.
// Auditing이란 엔티티의 생성일시, 생성자, 수정일시, 수정자를 자동으로 관리해주는 기능이다.
// 테스트코드에서 Auditing이 있어야지만 테스트코드가 정상적으로 동작한다.
@MappedSuperclass
@ToString
@Getter
public abstract class AuditingFields {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @CreatedDate
    @Column(nullable = false,updatable = false)
    private LocalDateTime createdAt; // 생성일시

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @CreatedBy
    @Column(nullable = false, updatable = false,length = 100)
    public String createdBy; //생성자

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modifiedAt; // 수정일시


    @LastModifiedBy
    @Column(nullable = false, length = 100)
    public String modifiedBy; // 수정자
}

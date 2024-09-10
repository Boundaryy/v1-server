package com.boundary.boundarybackend.domain.situation.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Situation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private String threadId;

    private String parentId;

    private String childId;

    @CreatedDate
    private LocalDateTime createdAt;

    @Builder
    public Situation(String content, String parentId, String childId) {
        this.content = content;
        this.parentId = parentId;
        this.childId = childId;
    }
}

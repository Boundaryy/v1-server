package com.boundary.boundarybackend.domain.sst.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Sst {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String threadId;

    @Column(columnDefinition = "TEXT")
    private String feedBack;

    private LocalDateTime finishTime;

    @Builder
    public Sst(Long userId, String threadId){
        this.userId = userId;
        this.threadId = threadId;
    }

    public void finishThread(String feedBack, LocalDateTime finishTime) {
        this.feedBack = feedBack;
        this.finishTime = finishTime;
    }
}

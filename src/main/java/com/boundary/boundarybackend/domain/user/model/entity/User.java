package com.boundary.boundarybackend.domain.user.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor // 기본 생성자 추가
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK

    private String refreshToken; // 리프레시 토큰

    private String name; // 이름

    private Integer age; // 나이

    private String gender; // 성별

    private String userId; // 아이디

    private String password; // 비밀번호
}

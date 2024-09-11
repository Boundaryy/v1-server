package com.boundary.boundarybackend.domain.user.model.entity;

import com.boundary.boundarybackend.domain.user.model.dto.vo.MemberRole;
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

    private String phoneNum; // 전화번호

    private String gender; // 성별

    @Column(unique = true)
    private String userId; // 아이디

    private String password; // 비밀번호

    @Enumerated(EnumType.STRING)
    private MemberRole role; // 부모or아이

    private Integer point; // 포인트

    @Column(unique = true)
    private String childId; // 아이 아이디
}

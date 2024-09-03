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
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK

    private Long userId; // 유저 ID

    private String attendanceDate; // 출석 날짜
}

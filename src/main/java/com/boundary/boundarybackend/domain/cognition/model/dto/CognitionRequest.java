package com.boundary.boundarybackend.domain.cognition.model.dto;

import com.boundary.boundarybackend.domain.user.model.dto.vo.MemberRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CognitionRequest
{
    @Schema(description = "더해질 포인트")
    private Integer addPoint; // 포인트
}


package com.boundary.boundarybackend.domain.situation.model.dto;

import com.boundary.boundarybackend.domain.situation.model.entity.Situation;

import java.time.LocalDateTime;

public record GetSituationResponse(
        Long situationId,
        String content,
        String parentId,
        String childId,
        LocalDateTime createdAt
) {
    public static GetSituationResponse from(Situation situation) {
        return new GetSituationResponse(
                situation.getId(),
                situation.getContent(),
                situation.getParentId(),
                situation.getChildId(),
                situation.getCreatedAt()
        );
    }
}

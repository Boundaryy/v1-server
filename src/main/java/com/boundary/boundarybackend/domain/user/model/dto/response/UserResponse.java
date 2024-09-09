package com.boundary.boundarybackend.domain.user.model.dto.response;

public record UserResponse(
        String name,
        String userId,
        int point,
        String childId
) {}
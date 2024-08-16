package com.boundary.boundarybackend.common.dto;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {
}
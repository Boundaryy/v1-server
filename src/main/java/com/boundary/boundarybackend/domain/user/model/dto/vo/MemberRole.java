package com.boundary.boundarybackend.domain.user.model.dto.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberRole
{
    Child("Child"),
    Parent("Parent");

    private final String role;
}


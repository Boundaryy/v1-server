package com.boundary.boundarybackend.domain.user.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest
{
    @Schema(description = "ID")
    private String userId;

    @Schema(description = "패스워드")
    private String password;
}

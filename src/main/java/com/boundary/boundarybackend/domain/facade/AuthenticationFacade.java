package com.boundary.boundarybackend.domain.facade;


import com.boundary.boundarybackend.common.exception.BusinessException;
import com.boundary.boundarybackend.common.exception.ErrorCode;
import com.boundary.boundarybackend.domain.user.model.dto.request.*;
import com.boundary.boundarybackend.domain.user.model.dto.response.LoginResponse;
import com.boundary.boundarybackend.domain.user.service.AuthService;
import com.boundary.boundarybackend.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.boundary.boundarybackend.common.dto.TokenResponse;

@Component
@RequiredArgsConstructor
public class AuthenticationFacade {
    private final AuthService authService;
    private final UserService userService;

    @Transactional
    public LoginResponse login(LoginRequest req) {
        return authService.login(req.getUserId(), req.getPassword());
    }

    @Transactional
    public void logout(Long memberId) {
        authService.logout(memberId);
    }

    @Transactional
    public TokenResponse refreshToken(TokenRefreshRequest tokenRefreshRequest) {
        return authService.refreshToken(tokenRefreshRequest);
    }

    public void signUp(SignUpRequest req) {
        try {
            userService.create(req);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.DUPLICATED);
        }
    }
}
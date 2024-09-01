package com.boundary.boundarybackend.domain.user.controller;

import com.boundary.boundarybackend.common.dto.TokenResponse;
import com.boundary.boundarybackend.domain.facade.AuthenticationFacade;
import com.boundary.boundarybackend.domain.user.model.dto.request.*;
import com.boundary.boundarybackend.domain.user.model.dto.response.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.boundary.boundarybackend.common.util.AuthenticationUtil.getMemberId;

@Tag(name = "인증")
@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationFacade authenticationFacade;
    @Operation(summary = "회원가입")
    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(
            @Valid @RequestBody SignUpRequest req) {
        authenticationFacade.signUp(req);
        return new ResponseEntity<>("회원가입이 완료되었습니다.", HttpStatus.OK);
    }

    @Operation(summary = "회원 로그인")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest req) {
        return ResponseEntity.ok(authenticationFacade.login(req));
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        authenticationFacade.logout(getMemberId());
        return new ResponseEntity<>("로그아웃이 완료되었습니다.", HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "토큰 갱신")
    @PostMapping("/refresh-token")
    public ResponseEntity<TokenResponse> refreshToken(
            @Valid @RequestBody TokenRefreshRequest tokenRefreshRequest) {
        return ResponseEntity.ok(authenticationFacade.refreshToken(tokenRefreshRequest));
    }
}

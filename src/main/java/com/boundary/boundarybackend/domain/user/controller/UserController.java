package com.boundary.boundarybackend.domain.cognition.controller;

import com.boundary.boundarybackend.common.dto.TokenResponse;
import com.boundary.boundarybackend.domain.cognition.model.dto.CognitionRequest;
import com.boundary.boundarybackend.domain.facade.AuthenticationFacade;
import com.boundary.boundarybackend.domain.facade.CognitionFacade;
import com.boundary.boundarybackend.domain.facade.UserFacade;
import com.boundary.boundarybackend.domain.user.model.dto.request.*;
import com.boundary.boundarybackend.domain.user.model.dto.response.LoginResponse;
import com.boundary.boundarybackend.domain.user.model.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.boundary.boundarybackend.common.util.AuthenticationUtil.getMemberId;

@Tag(name = "출석")
@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserFacade userFacade;
    @Operation(summary = "유저 정보 확인")
    @GetMapping("/user")
    public ResponseEntity<User> getUser() {
      var user = userFacade.getUser(getMemberId());
        return ResponseEntity.ok(user);
    }
}
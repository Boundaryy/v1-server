package com.boundary.boundarybackend.domain.cognition.controller;

import com.boundary.boundarybackend.common.dto.TokenResponse;
import com.boundary.boundarybackend.domain.cognition.model.dto.CognitionRequest;
import com.boundary.boundarybackend.domain.facade.AuthenticationFacade;
import com.boundary.boundarybackend.domain.facade.CognitionFacade;
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

@Tag(name = "코그니션")
@Slf4j
@RestController
@RequiredArgsConstructor
public class CognitionController {

    private final CognitionFacade cognitionFacade;
    @Operation(summary = "코그니션")
    @PostMapping("/cognition")
    public ResponseEntity<String> cognition(
            @Valid @RequestBody CognitionRequest req) {
        cognitionFacade.cognition(req,getMemberId());
        return new ResponseEntity<>("포인트 추가가 완료되었습니다.", HttpStatus.OK);
    }
}
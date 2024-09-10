package com.boundary.boundarybackend.domain.situation.controller;

import com.boundary.boundarybackend.domain.situation.model.dto.CreateSituationRequest;
import com.boundary.boundarybackend.domain.situation.model.dto.GetSituationResponse;
import com.boundary.boundarybackend.domain.situation.service.CommandSituationService;
import com.boundary.boundarybackend.domain.situation.service.QuerySituationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.boundary.boundarybackend.common.util.AuthenticationUtil.getMemberId;
import static com.boundary.boundarybackend.common.util.AuthenticationUtil.getMemberRole;

@Tag(name = "상황")
@RestController
@RequiredArgsConstructor
@RequestMapping("/situations")
public class SituationController {

    private final CommandSituationService commandSituationService;
    private final QuerySituationService querySituationService;

    @Operation(summary = "상황 생성")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Long createSituation(
            @RequestBody CreateSituationRequest createSituationRequest
    ){
        return commandSituationService.createSituation(createSituationRequest, getMemberId());
    }

    @Operation(summary = "상황 조회")
    @GetMapping("/{situationId}")
    public ResponseEntity<GetSituationResponse> getSituation(@PathVariable Long situationId){
        return querySituationService.getSituation(situationId);
    }

    @Operation(summary = "상황 전체 조회")
    @GetMapping
    public ResponseEntity<List<GetSituationResponse>> getAllSituation(){
        return querySituationService.getAllSituation(getMemberId(),getMemberRole());
    }

    @Operation(summary = "상황 삭제")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping("/{situationId}")
    public void deleteSituation(@PathVariable Long situationId){
        commandSituationService.deleteSituation(situationId);
    }


}

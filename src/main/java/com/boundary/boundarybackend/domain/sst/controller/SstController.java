package com.boundary.boundarybackend.domain.sst.controller;

import com.boundary.boundarybackend.domain.sst.model.dto.external.BotChatResponse;
import com.boundary.boundarybackend.domain.sst.model.dto.external.ChatSstThreadRequest;
import com.boundary.boundarybackend.domain.sst.model.dto.external.GetFeedBackResponse;
import com.boundary.boundarybackend.domain.sst.model.dto.external.SstThreadResponse;
import com.boundary.boundarybackend.domain.sst.service.CommandSstService;
import com.boundary.boundarybackend.domain.sst.service.QuerySstService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.boundary.boundarybackend.common.util.AuthenticationUtil.getMemberId;

@Tag(name = "SST")
@RestController
@RequiredArgsConstructor
@RequestMapping("/stt")
public class SstController {

    private final CommandSstService commandSstService;
    private final QuerySstService querySstService;

    @Operation(summary = "SST 스레드 생성")
    @PostMapping("/{situationId}")
    public ResponseEntity<SstThreadResponse> createSstThread(
            @PathVariable Long situationId
    ){
        return commandSstService.createThread(situationId, getMemberId());
    }

    @Operation(summary = "SST 사용자 채팅")
    @PostMapping("/threads/{threadId}")
    public ResponseEntity<BotChatResponse> chatSstThread(
            @RequestBody ChatSstThreadRequest chatSstThreadRequest, @PathVariable String threadId
            ){
        return commandSstService.chatThread(chatSstThreadRequest, threadId);

    }

    @Operation(summary = "SST 학습 종료")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping("/threads/{threadId}")
    public void finishSstThread(
            @PathVariable String threadId
    ){
        commandSstService.finishThread(threadId);
    }

    @Operation(summary = "SST 피드백 조회")
    @GetMapping("/threads/{threadId}")
    public ResponseEntity<GetFeedBackResponse> getSstThreads(
            @PathVariable String threadId
    ){
        return querySstService.getFeedBack(threadId);

    }
}
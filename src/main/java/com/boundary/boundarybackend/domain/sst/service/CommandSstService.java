package com.boundary.boundarybackend.domain.sst.service;

import com.boundary.boundarybackend.common.exception.BusinessException;
import com.boundary.boundarybackend.common.exception.ErrorCode;
import com.boundary.boundarybackend.domain.situation.model.entity.Situation;
import com.boundary.boundarybackend.domain.situation.repository.SituationRepository;
import com.boundary.boundarybackend.domain.sst.model.dto.external.BotChatResponse;
import com.boundary.boundarybackend.domain.sst.model.dto.external.ChatSstThreadRequest;
import com.boundary.boundarybackend.domain.sst.model.dto.external.SstThreadResponse;
import com.boundary.boundarybackend.domain.sst.model.dto.internal.BotMessageResponse;
import com.boundary.boundarybackend.domain.sst.model.dto.internal.CreateSttThreadRequest;
import com.boundary.boundarybackend.domain.sst.model.dto.internal.FeedBackResponse;
import com.boundary.boundarybackend.domain.sst.model.entity.Sst;
import com.boundary.boundarybackend.domain.sst.repository.SstRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;

@Service
@Slf4j
@Transactional
public class CommandSstService {
    private final SstRepository sstRepository;
    private final SituationRepository situationRepository;
    private final RestClient restClient;
    private final String uriBase;

    public CommandSstService(SstRepository sstRepository, SituationRepository situationRepository,
                             RestClient restClient, @Value("${flask.url.base}") String uriBase) {
        this.sstRepository = sstRepository;
        this.situationRepository = situationRepository;
        this.restClient = restClient;
        this.uriBase = uriBase;
    }

    public ResponseEntity<SstThreadResponse> createThread(Long situationId, Long userId) {
        Situation situation = situationRepository.findById(situationId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

        ResponseEntity<SstThreadResponse> rc = restClient.post()
                .uri(uriBase+"/api/stt/threads")
                .contentType(MediaType.APPLICATION_JSON)
                .body(CreateSttThreadRequest.from(situation))
                .retrieve()
                .toEntity(SstThreadResponse.class);

        log.info("SST thread created: {}", rc);
        log.info("SST thread created(rc.getBody): {}", rc.getBody());

        Sst sst = Sst.builder()
                .userId(userId)
                .threadId(rc.getBody().threadId())
                .build();
        sstRepository.save(sst);

        return ResponseEntity.ok(rc.getBody());
    }

    public ResponseEntity<BotChatResponse> chatThread(ChatSstThreadRequest request, String threadId){
        ResponseEntity<BotMessageResponse> rc = restClient.post()
                .uri(uriBase+"/api/stt/threads/"+threadId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .toEntity(BotMessageResponse.class);



        return ResponseEntity.ok(BotChatResponse.from(rc.getBody()));


    }

    public void finishThread(String threadId) {

        ResponseEntity<FeedBackResponse> rc = restClient.delete()
                .uri(uriBase+"/api/stt/threads/"+threadId)
                .retrieve()
                .toEntity(FeedBackResponse.class);
        Sst s = sstRepository.findByThreadId(threadId).orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));
        s.finishThread(rc.getBody().feedBack(), LocalDateTime.now());
    }

}

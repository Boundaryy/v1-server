package com.boundary.boundarybackend.domain.sst.service;

import com.boundary.boundarybackend.common.exception.BusinessException;
import com.boundary.boundarybackend.common.exception.ErrorCode;
import com.boundary.boundarybackend.domain.sst.model.dto.external.GetFeedBackResponse;
import com.boundary.boundarybackend.domain.sst.model.dto.external.GetChildEducationResponse;
import com.boundary.boundarybackend.domain.sst.model.entity.Sst;
import com.boundary.boundarybackend.domain.sst.repository.SstRepository;
import com.boundary.boundarybackend.domain.user.model.entity.User;
import com.boundary.boundarybackend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class QuerySstService {
    private final SstRepository sstRepository;
    private final UserRepository userRepository;

    public ResponseEntity<GetFeedBackResponse> getFeedBack(String threadId) {
        Sst s = sstRepository.findByThreadId(threadId).orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));
        return ResponseEntity.ok(GetFeedBackResponse.from(s));
    }
    public ResponseEntity<List<GetChildEducationResponse>> getChildEducation(Long parentId) {
        // 부모 ID로 User 엔티티 조회
        Optional<User> parentUserOpt = userRepository.findById(parentId);

        if (parentUserOpt.isEmpty()) {
            // 부모를 찾을 수 없는 경우 처리
            return ResponseEntity.notFound().build();
        }

        User parentUser = parentUserOpt.get();
        log.info("parentUser: " + parentUser);
        String childId = parentUser.getChildId();
        log.info("childId: " + childId);

        // User 테이블에서 childId가 UserId인 레코드를 조회
        Optional<User> childUserOpt = userRepository.findByUserId(childId);

        if (childUserOpt.isEmpty()) {
            log.info("해당 childId에 해당하는 유저가 없습니다.");
            return ResponseEntity.notFound().build();
        }

        User childUser = childUserOpt.get();
        Long childPrimaryKey = childUser.getId();
        log.info("childPrimaryKey: " + childPrimaryKey);

        // childId를 사용하여 Sst 엔티티 다중 조회
        List<Sst> sstList = sstRepository.findAllByUserId(childPrimaryKey);

        if (sstList.isEmpty()) {
            log.error("sst entity를 찾을 수 없음");
            throw new BusinessException(ErrorCode.ENTITY_NOT_FOUND);
        }

        // 각 Sst 엔티티를 GetChildEducationResponse DTO로 변환하여 리스트로 반환
        List<GetChildEducationResponse> responses = sstList.stream()
                .map(sst -> new GetChildEducationResponse(
                        sst.getThreadId(),
                        sst.getFeedBack(),
                        sst.getFinishTime()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }


}

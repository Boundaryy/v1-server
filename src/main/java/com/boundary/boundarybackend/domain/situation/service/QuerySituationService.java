package com.boundary.boundarybackend.domain.situation.service;

import com.boundary.boundarybackend.common.exception.BusinessException;
import com.boundary.boundarybackend.common.exception.ErrorCode;
import com.boundary.boundarybackend.domain.situation.model.dto.GetSituationResponse;
import com.boundary.boundarybackend.domain.situation.model.entity.Situation;
import com.boundary.boundarybackend.domain.situation.repository.SituationRepository;
import com.boundary.boundarybackend.domain.user.model.dto.vo.MemberRole;
import com.boundary.boundarybackend.domain.user.model.entity.User;
import com.boundary.boundarybackend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuerySituationService {
    private final SituationRepository situationRepository;
    private final UserRepository userRepository;

    public ResponseEntity<GetSituationResponse> getSituation(Long situationId){
        Situation s = situationRepository.findById(situationId).orElseThrow(
                () -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND)
        );

        return ResponseEntity.ok(GetSituationResponse.from(s));
    }

    public ResponseEntity<List<GetSituationResponse>> getAllSituation(Long userId, MemberRole memberRole){
        User user = userRepository.findById(userId).orElseThrow(
                () -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));
        String stId = user.getUserId();
        List<Situation> ls;
        if (memberRole.equals(MemberRole.Parent)){
            ls = situationRepository.findAllByParentId(stId);
        }
        else {
            ls = situationRepository.findAllByChildId(stId);
        }
        return ResponseEntity.ok(ls.stream().map(GetSituationResponse::from).toList());
    }

}

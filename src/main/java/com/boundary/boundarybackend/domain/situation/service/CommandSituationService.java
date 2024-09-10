package com.boundary.boundarybackend.domain.situation.service;

import com.boundary.boundarybackend.common.exception.BusinessException;
import com.boundary.boundarybackend.common.exception.ErrorCode;
import com.boundary.boundarybackend.domain.situation.model.dto.CreateSituationRequest;
import com.boundary.boundarybackend.domain.situation.model.entity.Situation;
import com.boundary.boundarybackend.domain.situation.repository.SituationRepository;
import com.boundary.boundarybackend.domain.user.model.entity.User;
import com.boundary.boundarybackend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommandSituationService {

    private final SituationRepository situationRepository;
    private final UserRepository userRepository;

    public Long createSituation(CreateSituationRequest createSituationRequest, Long memberId) {
        User user = userRepository.findById(memberId).orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));
        String parentId = user.getUserId();
        String childId = user.getChildId();

        Situation situation = Situation.builder()
                .content(createSituationRequest.situation())
                .parentId(parentId)
                .childId(childId)
                .build();
        situationRepository.save(situation);
        return situation.getId();
    }

    public void deleteSituation(Long id) {
        situationRepository.deleteById(id);
    }
}

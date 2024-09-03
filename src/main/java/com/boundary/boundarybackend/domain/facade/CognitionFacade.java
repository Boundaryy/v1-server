package com.boundary.boundarybackend.domain.facade;

import com.boundary.boundarybackend.domain.cognition.service.CognitionService;
import com.boundary.boundarybackend.domain.cognition.model.dto.CognitionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CognitionFacade {
    private final CognitionService cognitionService;

    @Transactional
    public void cognition(CognitionRequest req, Long id) {
        // CognitionService를 사용해 User의 포인트를 업데이트한다.
        cognitionService.updateUserPoints(req, id);
    }
}

package com.boundary.boundarybackend.domain.cognition.service;

import com.boundary.boundarybackend.domain.cognition.model.dto.CognitionRequest;
import com.boundary.boundarybackend.domain.user.model.entity.User;
import com.boundary.boundarybackend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CognitionService {

    private final UserRepository userRepository;

    @Transactional
    public void updateUserPoints(CognitionRequest req, Long id) {
        // User를 ID로 찾는다.
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

        // CognitionRequest에서 얻은 addPoint를 User의 point에 더한다.
        Integer newPoint = user.getPoint() + req.getAddPoint();
        user.setPoint(newPoint);

        // 변경된 User 정보를 저장한다.
        userRepository.save(user);
    }
}

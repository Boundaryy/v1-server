package com.boundary.boundarybackend.domain.user.service;

import com.boundary.boundarybackend.domain.user.repository.UserRepository;
import com.boundary.boundarybackend.common.exception.BusinessException;
import com.boundary.boundarybackend.common.exception.ErrorCode;
import com.boundary.boundarybackend.domain.user.model.dto.request.SignUpRequest;
import com.boundary.boundarybackend.domain.user.model.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public void create(SignUpRequest req) {
        try {
            // 회원 저장
            User user = userRepository.save(User.builder()
                    .name(req.getName()) // 이름
                    .age(req.getAge()) // 나이
                    .phoneNum(req.getPhoneNum()) // 전화번호
                    .gender(req.getGender()) // 성별
                    .userId(req.getUserId()) // 사용자 아이디
                    .password(new BCryptPasswordEncoder().encode(req.getPassword())) // 비밀번호
                    .role(req.getRole()) // 부모or아이 권한
                    .point(req.getPoint()) // 포인트
                    .build()
            );
        } catch (Exception e) {
            log.error("회원 생성 중 오류 발생: {}", e.getMessage());
            throw new BusinessException(ErrorCode.UNKNOWN_ERROR);
        }
    }
}

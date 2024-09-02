package com.boundary.boundarybackend.domain.user.service;


import com.boundary.boundarybackend.common.dto.TokenResponse;
import com.boundary.boundarybackend.common.exception.BusinessException;
import com.boundary.boundarybackend.common.exception.ErrorCode;
import com.boundary.boundarybackend.common.jwt.Jwt;
import com.boundary.boundarybackend.domain.user.model.dto.request.TokenRefreshRequest;
import com.boundary.boundarybackend.domain.user.model.dto.response.LoginResponse;
import com.boundary.boundarybackend.domain.user.model.dto.vo.MemberRole;
import com.boundary.boundarybackend.domain.user.model.entity.User;
import com.boundary.boundarybackend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final Jwt jwt;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public LoginResponse login(String userId, String password) {
        try {
            User user = userRepository.findByUserId(userId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.UNAUTHORIZED));

            // 입력된 비밀번호와 저장된 비밀번호 해시를 비교하여 일치 여부를 확인합니다.
            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new BusinessException(ErrorCode.UNAUTHORIZED);
            }

            // 비밀번호가 일치하면 로그인 응답 객체를 생성하여 반환합니다.
            return getLoginResponse(user);
        } catch (BusinessException e) {
            log.error("로그인 실패: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("로그인 중 알 수 없는 오류 발생: {}", e.getMessage());
            throw new BusinessException(ErrorCode.UNKNOWN_ERROR);
        }
    }

    @Transactional
    public void logout(Long memberId) {
        try {
            User user = userRepository.findById(memberId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.ENTITY_NOT_FOUND));

            user.setRefreshToken("");
        } catch (BusinessException e) {
            log.error("로그아웃 실패: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("로그아웃 중 알 수 없는 오류 발생: {}", e.getMessage());
            throw new BusinessException(ErrorCode.UNKNOWN_ERROR);
        }
    }

    public TokenResponse publishToken(User user) {
        try {
            TokenResponse tokenResponse = jwt.generateAllToken(
                    Jwt.Claims.from(
                            user.getId(),
                            user.getRole())

            );

            user.setRefreshToken(tokenResponse.refreshToken());
            userRepository.save(user);

            return tokenResponse;
        } catch (Exception e) {
            log.error("토큰 발행 중 오류 발생: {}", e.getMessage());
            throw new BusinessException(ErrorCode.UNKNOWN_ERROR);
        }
    }

    @Transactional
    public TokenResponse refreshToken(TokenRefreshRequest tokenRefreshRequest) {
        try {
            var user = userRepository.findByRefreshToken(tokenRefreshRequest.refreshToken());
            if (user.isEmpty()) {
                throw new AccessDeniedException("refresh token 이 만료되었습니다.");
            }
            MemberRole role;
            Long memberId;

            try {
                Jwt.Claims claims = jwt.verify(user.get().getRefreshToken());
                memberId = claims.getMemberId();
                role = claims.getRole();
            } catch (Exception e) {
                log.warn("Jwt 처리중 문제가 발생하였습니다 : {}", e.getMessage());
                throw new AccessDeniedException("Jwt 처리중 문제가 발생하였습니다.");
            }

            TokenResponse tokenResponse = jwt.generateAllToken(Jwt.Claims.from(memberId,role));
            user.get().setRefreshToken(tokenResponse.refreshToken());

            return tokenResponse;
        } catch (AccessDeniedException e) {
            log.error("토큰 갱신 실패: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("토큰 갱신 중 알 수 없는 오류 발생: {}", e.getMessage());
            throw new BusinessException(ErrorCode.UNKNOWN_ERROR);
        }
    }

    private LoginResponse getLoginResponse(User user) {
        try {
            var tokens = publishToken(user);
            return new LoginResponse(tokens);
        } catch (Exception e) {
            log.error("로그인 응답 생성 중 오류 발생: {}", e.getMessage());
            throw new BusinessException(ErrorCode.UNKNOWN_ERROR);
        }
    }
}

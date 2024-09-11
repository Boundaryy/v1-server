package com.boundary.boundarybackend.common.util;

import com.boundary.boundarybackend.common.jwt.Jwt;
import com.boundary.boundarybackend.domain.user.model.dto.vo.MemberRole;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 인증 관련 유틸리티 클래스입니다.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthenticationUtil {

    /**
     * 인증된 사용자의 회원 ID를 가져옵니다.
     * @return 인증된 사용자의 회원 ID. 인증되지 않은 경우 null을 반환합니다.
     */
    public static Long getMemberId() {
        var anonymous = String.valueOf(isAnonymous());
        log.warn(anonymous);
        if (isAnonymous()) {
            return null;
        }
        return (Long) getAuthentication().getPrincipal();
    }

    /**
     * 인증된 사용자의 역할을 가져옵니다.
     * @return 사용자의 역할(Set 형태). 인증되지 않은 경우 빈 Set을 반환합니다.
     */
    public static MemberRole getMemberRole() {
        if (isAnonymous()) {
            log.info("unknown");
            return MemberRole.Child;  // 익명 사용자는 기본적으로 'Child' 역할로 처리
        }

        Authentication authentication = getAuthentication();

        // JWT에서 역할 정보 추출
        if (authentication.getPrincipal() instanceof Jwt.Claims) {
            Jwt.Claims claims = (Jwt.Claims) authentication.getPrincipal();
            return claims.getRole();  // JWT에 저장된 역할 반환
        }

        // 일반적으로 Spring Security의 GrantedAuthority에서 역할을 추출
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(role -> MemberRole.valueOf(role))
                .findFirst()
                .orElse(MemberRole.Child);  // 기본값으로 'Child' 반환
    }

    /**
     * 사용자가 익명 사용자(인증되지 않은 사용자)인지 확인합니다.
     * @return 익명 사용자이면 true, 그렇지 않으면 false를 반환합니다.
     */
    public static boolean isAnonymous() {
        Authentication authentication = getAuthentication();
        return authentication == null || authentication.getPrincipal().equals("anonymousUser");
    }

    /**
     * 현재 인증 객체를 가져옵니다.
     * @return 현재 인증 객체
     */
    private static Authentication getAuthentication() {
        var context = SecurityContextHolder.getContext();
        log.info("Context :"+ context);
        var result = context.getAuthentication();
        log.info("result : " + result);
        return result;
    }
}


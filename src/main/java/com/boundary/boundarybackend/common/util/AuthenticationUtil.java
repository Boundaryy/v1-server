package com.boundary.boundarybackend.common.util;

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
            return MemberRole.Child;
        }
        Authentication authentication = getAuthentication();
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(role -> MemberRole.valueOf(role))
                .findFirst()
                .orElse(MemberRole.Child);
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
     * 주어진 회원 ID가 현재 인증된 사용자와 일치하는지 확인합니다.
     * @param memberId 확인할 회원 ID
     * @return 일치하면 true, 그렇지 않으면 false를 반환합니다.
     */
    public static boolean isValidAccess(Long memberId) {
        return Objects.equals(AuthenticationUtil.getMemberId(), memberId);
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

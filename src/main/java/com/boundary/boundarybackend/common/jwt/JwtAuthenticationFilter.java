package com.boundary.boundarybackend.common.jwt;

// 필요한 패키지 임포트
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

// 로그를 위한 어노테이션
@Slf4j
// 생성자 주입을 위한 어노테이션
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilter {

    // Jwt 객체 주입
    private final Jwt jwt;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // ServletRequest를 HttpServletRequest로 변환
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        // ServletResponse를 HttpServletResponse로 변환
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        // 현재 인증 상태를 확인
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            log.info("getAuthentication is null");
            // 요청에서 토큰 추출
            String token = getAccessToken(httpServletRequest);

            log.info("토큰 : "+token);

            if (token != null) {
                try {
                    // 토큰 검증 및 클레임 추출
                    Jwt.Claims claims = verify(token);
                    Long memberId = claims.memberId;
                    log.info("memberId : "+memberId);
//                    List<GrantedAuthority> authorities = getAuthorities(claims);

                    // 클레임이 유효하고 권한이 있으면 인증 객체 생성
                    if (memberId != null) {
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                                new UsernamePasswordAuthenticationToken(memberId, null);
                        log.info("usernamePasswordAuthenticationToken" + usernamePasswordAuthenticationToken.toString());
                        // 인증 객체를 SecurityContextHolder에 설정
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    }
                } catch (Exception e) {
                    // 예외 발생 시 경고 로그 출력
                    log.warn("Jwt 처리중 문제가 발생하였습니다 : {}", e.getMessage());
                }
            }
        } else {
            // 이미 인증 객체가 존재할 경우 디버그 로그 출력
            log.debug("이미 인증 객체가 존재합니다 : {}",
                    SecurityContextHolder.getContext().getAuthentication());
        }
        // 필터 체인 계속 진행
        chain.doFilter(httpServletRequest, httpServletResponse);t a
    }

    // 요청에서 액세스 토큰을 추출
    private String getAccessToken(HttpServletRequest request) {
        String accessToken = request.getHeader("access_token");

        // 액세스 토큰이 존재하고 비어있지 않으면 디코딩
        if (accessToken != null && !accessToken.isBlank()) {
            try {
                return URLDecoder.decode(accessToken, StandardCharsets.UTF_8);
            } catch (Exception e) {
                // 디코딩 실패 시 경고 로그 출력
                log.warn(e.getMessage(), e);
            }
        }
        return null;
    }

    // JWT 토큰을 검증하고 클레임을 반환
    private Jwt.Claims verify(String token) {
        return jwt.verify(token);
    }

    // 클레임에서 권한 리스트를 생성
//    private List<GrantedAuthority> getAuthorities(Jwt.Claims claims) {
//        String[] roles = claims.roles;
//        if (roles == null || roles.length == 0) {
//            return emptyList();
//        }
//        return Arrays.stream(roles)
//                .map(SimpleGrantedAuthority::new)
//                .collect(Collectors.toList());
//    }
}


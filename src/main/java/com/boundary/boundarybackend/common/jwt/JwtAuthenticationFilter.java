package com.boundary.boundarybackend.common.jwt;

import com.boundary.boundarybackend.domain.user.model.dto.vo.MemberRole;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilter {

    private final Jwt jwt;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            log.info("getAuthentication is null");
            String token = getAccessToken(httpServletRequest);

            log.info("토큰 : "+token);

            if (token != null) {
                try {
                    Jwt.Claims claims = verify(token);
                    Long memberId = claims.getMemberId();
                    MemberRole role = claims.getRole();

                    log.info("memberId : "+memberId);
                    log.info("role : "+role);

                    if (memberId != null) {
                        Collection<GrantedAuthority> authorities = getAuthorities(role);
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(memberId, null, authorities);
                        log.info("authentication : "+authentication.toString());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                } catch (Exception e) {
                    log.warn("Jwt 처리중 문제가 발생하였습니다 : {}", e.getMessage());
                }
            }
        } else {
            log.debug("이미 인증 객체가 존재합니다 : {}",
                    SecurityContextHolder.getContext().getAuthentication());
        }
        chain.doFilter(httpServletRequest, httpServletResponse);
    }

    private String getAccessToken(HttpServletRequest request) {
        log.warn("리퀘스트 :" +request.toString());
        String accessToken = request.getHeader("access_token");
        log.info("accessToken : "+ accessToken);

        if (accessToken != null && !accessToken.isBlank()) {
            try {
                return URLDecoder.decode(accessToken, StandardCharsets.UTF_8);
            } catch (Exception e) {
                log.warn(e.getMessage(), e);
            }
        }
        return null;
    }

    private Jwt.Claims verify(String token) {
        return jwt.verify(token);
    }

    private Collection<GrantedAuthority> getAuthorities(MemberRole role) {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }
}


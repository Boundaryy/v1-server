package com.boundary.boundarybackend.common.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.boundary.boundarybackend.common.dto.TokenResponse;
import com.boundary.boundarybackend.domain.user.model.dto.vo.MemberRole;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

public class Jwt {
    private final String issuer;
    private final int tokenExpire;
    private final int refreshTokenExpire;
    private final Algorithm algorithm;
    private final JWTVerifier jwtVerifier;

    public Jwt(String clientSecret, String issuer, int tokenExpire, int refreshTokenExpire) {
        this.issuer = issuer;
        this.tokenExpire = tokenExpire;
        this.refreshTokenExpire = refreshTokenExpire;
        this.algorithm = Algorithm.HMAC512(clientSecret);
        this.jwtVerifier = JWT.require(algorithm)
                .withIssuer(issuer).build();
    }

    public String generateAccessToken(Claims claims) {
        return sign(claims, tokenExpire);
    }

    public String generateRefreshToken(Claims claims) {
        return sign(claims, refreshTokenExpire);
    }

    public TokenResponse generateAllToken(Claims claims) {
        return new TokenResponse(generateAccessToken(claims), generateRefreshToken(claims));
    }

    private String sign(Claims claims, int expireTime) {
        Date now = new Date();
        return JWT.create()
                .withIssuer(issuer)
                .withIssuedAt(now)
                .withExpiresAt(new Date(now.getTime() + expireTime * 1000L))
                .withClaim("memberId", claims.memberId)
                .withClaim("role", claims.role.name()) // Enum 값을 문자열로 변환하여 저장
                .sign(algorithm);
    }

    public Claims verify(String token) {
        return new Claims(jwtVerifier.verify(token));
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Claims {
        Long memberId;
        MemberRole role;
        Date iat;
        Date exp;

        Claims(DecodedJWT decodedJwt) {
            Claim memberId = decodedJwt.getClaim("memberId");
            if (!memberId.isNull()) {
                this.memberId = memberId.asLong();
            }
            this.iat = decodedJwt.getIssuedAt();
            this.exp = decodedJwt.getExpiresAt();
        }

        public static Claims from(Long memberId,MemberRole role) {
            Claims claims = new Claims();
            claims.memberId = memberId;
            claims.role = role;
            return claims;
        }

        @Override
        public String toString() {
            return "Claims{"
                    + "memberId=" + memberId
                    + ", roles=" + role // 배열이 아니라면 직접 사용
                    + ", iat=" + iat
                    + ", exp=" + exp
                    + '}';
        }
    }
}

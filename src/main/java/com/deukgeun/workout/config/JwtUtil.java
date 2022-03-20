package com.deukgeun.workout.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.deukgeun.workout.auth.dto.VerifyResult;
import com.deukgeun.workout.user.domain.User;
import com.deukgeun.workout.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

public class JwtUtil {

    private static final Algorithm ALGORITHM = Algorithm.HMAC256("DEUKGEUN");
    private static final long AUTH_TIME = 60 * 30;
    private static final long REFRESH_TIME = 60 * 60 * 24 * 7;

    public static String makeAuthToken(User user) {
        return JWT.create().withSubject(user.getEmail())
                .withClaim("exp", Instant.now().getEpochSecond() + AUTH_TIME)
                .sign(ALGORITHM);
    }

    public static String makeRefreshToken(User user) {
        return JWT.create().withSubject(user.getEmail())
                .withClaim("exp", Instant.now().getEpochSecond() + REFRESH_TIME)
                .sign(ALGORITHM);
    }

    public static VerifyResult verify(String token) {
        try {
            DecodedJWT verify = JWT.require(ALGORITHM).build().verify(token);
            return VerifyResult.builder()
                    .success(true).email(verify.getSubject()).build();
        } catch (Exception e) {
            DecodedJWT decode = JWT.decode(token);
            return VerifyResult.builder()
                    .success(false).email(decode.getSubject()).build();
        }
    }

    public static String resolveAuthToken(HttpServletRequest request) {
        String bearer = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (bearer == null || !bearer.startsWith("Bearer ")) {
            return null;
        }
        return bearer.substring("Bearer ".length());
    }

    public static String resolveRefreshToken(HttpServletRequest request) {
        String bearer = request.getHeader("refresh_token");
        if (bearer == null || !bearer.startsWith("Bearer ")) {
            return null;
        }
        return bearer.substring("Bearer ".length());
    }

    public static String findEmailByToken(String token) {
        DecodedJWT verify = JWT.require(ALGORITHM).build().verify(token);
        return verify.getSubject();
    }
}

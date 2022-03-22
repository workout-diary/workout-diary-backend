package com.deukgeun.workout.auth.service;

import com.deukgeun.workout.auth.dto.JwtTokenDto;
import com.deukgeun.workout.auth.dto.VerifyResult;
import com.deukgeun.workout.config.JwtUtil;
import com.deukgeun.workout.redis.service.RedisService;
import com.deukgeun.workout.user.domain.User;
import com.deukgeun.workout.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class AuthService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private UserRepository userRepository;

    public JwtTokenDto refreshTokens(HttpServletRequest request) throws Exception {
        String authToken = JwtUtil.resolveAuthToken(request);
        String refreshToken = JwtUtil.resolveRefreshToken(request);
        VerifyResult verifyAuthResult = JwtUtil.verify(authToken);
        VerifyResult verifyRefreshResult = JwtUtil.verify(refreshToken);

        if (isRefreshAvailable(verifyAuthResult, verifyRefreshResult, refreshToken)) {
            User user = userRepository.findByEmail(verifyAuthResult.getEmail()).orElseThrow(() -> new Exception("User not found"));

            authToken = JwtUtil.makeAuthToken(user);
            refreshToken = JwtUtil.makeRefreshToken(user);

            redisService.setValue(refreshToken, user.getEmail());

            return JwtTokenDto.builder()
                    .authToken(authToken)
                    .refreshToken(refreshToken)
                    .build();
        } else {
            throw new Exception("Refresh Token is expired.");
        }
    }

    private boolean isRefreshAvailable(VerifyResult verifyAuthResult, VerifyResult verifyRefreshResult, String refreshToken) {
        return (verifyAuthResult.getEmail().equals(verifyRefreshResult.getEmail())) && verifyRefreshResult.isSuccess() && redisService.isRefreshTokenInRedis(refreshToken);
    }
}

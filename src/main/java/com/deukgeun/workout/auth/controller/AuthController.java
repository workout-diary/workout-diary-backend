package com.deukgeun.workout.auth.controller;

import com.deukgeun.workout.auth.dto.AccessTokenDto;
import com.deukgeun.workout.auth.dto.JwtTokenDto;
import com.deukgeun.workout.auth.dto.VerifyResult;
import com.deukgeun.workout.auth.service.AuthService;
import com.deukgeun.workout.config.JwtUtil;
import com.deukgeun.workout.redis.service.RedisService;
import com.deukgeun.workout.user.domain.User;
import com.deukgeun.workout.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private AuthService authService;

    @PostMapping("/kakao")
    public JwtTokenDto generateTokens(@RequestBody AccessTokenDto accessTokenDto) throws ParseException {
        User user = userService.saveOrUpdate(userService.getKakaoUser(accessTokenDto));
        String authToken = JwtUtil.makeAuthToken(user);
        String refreshToken = JwtUtil.makeRefreshToken(user);

        redisService.setValue(refreshToken, user.getEmail());

        return JwtTokenDto.builder()
                .authToken(authToken)
                .refreshToken(refreshToken)
                .build();
    }

    @GetMapping("/refresh")
    public JwtTokenDto refreshTokens(HttpServletRequest request) throws Exception {
        return authService.refreshTokens(request);
    }
}

package com.deukgeun.workout.auth.controller;

import com.deukgeun.workout.auth.dto.AccessToken;
import com.deukgeun.workout.auth.dto.JwtToken;
import com.deukgeun.workout.config.JwtUtil;
import com.deukgeun.workout.redis.service.RedisService;
import com.deukgeun.workout.user.domain.User;
import com.deukgeun.workout.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @PostMapping("kakao")
    public JwtToken generateTokens(@RequestBody AccessToken accessToken) throws ParseException {
        User user = userService.saveOrUpdate(userService.getKakaoUser(accessToken));
        String authToken = JwtUtil.makeAuthToken(user);
        String refreshToken = JwtUtil.makeRefreshToken(user);

        redisService.setValue(refreshToken, user.getEmail());

        return JwtToken.builder()
                .authToken(authToken)
                .refreshToken(refreshToken)
                .build()
                ;
    }
}

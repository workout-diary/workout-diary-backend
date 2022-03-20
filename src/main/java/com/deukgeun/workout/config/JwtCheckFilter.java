package com.deukgeun.workout.config;

import com.deukgeun.workout.auth.dto.VerifyResult;
import com.deukgeun.workout.redis.service.RedisService;
import com.deukgeun.workout.user.service.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtCheckFilter extends OncePerRequestFilter {

    private UserService userService;
    private RedisService redisService;

    public JwtCheckFilter(UserService userService, RedisService redisService) {
        this.userService = userService;
        this.redisService = redisService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {

        String authToken = JwtUtil.resolveAuthToken(request);
        String refreshToken = JwtUtil.resolveRefreshToken(request);

        if (authToken == null || refreshToken == null) {
            chain.doFilter(request, response);
            return;
        }

        VerifyResult verifyAuthToken = JwtUtil.verify(authToken);
        VerifyResult verifyRefreshToken = JwtUtil.verify(refreshToken);

        if (verifyAuthToken.isSuccess() && verifyRefreshToken.isSuccess() && redisService.isRefreshTokenInRedis(refreshToken)) {
            this.setAuthentication(verifyAuthToken);
        }
        chain.doFilter(request, response);
    }

    private void setAuthentication(VerifyResult verifyResult) {
        UserDetails userDetails = userService.loadUserByUsername(verifyResult.getEmail());
        UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(), null, userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(userToken);
    }
}

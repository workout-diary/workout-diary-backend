package com.deukgeun.workout.user.controller;

import com.deukgeun.workout.config.JwtUtil;
import com.deukgeun.workout.user.domain.User;
import com.deukgeun.workout.user.dto.OAuth2User;
import com.deukgeun.workout.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/me")
    public OAuth2User getUserInfo(HttpServletRequest request) {
        String authToken = JwtUtil.resolveAuthToken(request);
        String email = JwtUtil.findEmailByToken(authToken);

        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
        return OAuth2User.builder()
                .name(user.getName())
                .id(user.getId())
                .email(user.getEmail())
                .image(user.getImage())
                .build();
    }
}

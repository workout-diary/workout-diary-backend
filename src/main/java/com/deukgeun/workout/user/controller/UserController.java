package com.deukgeun.workout.user.controller;

import com.deukgeun.workout.config.JwtUtil;
import com.deukgeun.workout.user.domain.User;
import com.deukgeun.workout.user.domain.UserProperty;
import com.deukgeun.workout.user.dto.UserDto;
import com.deukgeun.workout.user.dto.UserPropertyDto;
import com.deukgeun.workout.user.repository.UserRepository;
import com.deukgeun.workout.user.service.UserPropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserPropertyService userPropertyService;

    @PostMapping("me/property/{id}")
    public UserPropertyDto setUserProperty(
            @PathVariable("id") Long id,
            @RequestBody UserPropertyDto userPropertyDto) throws Exception {
        UserProperty userProperty = userPropertyService.saveOrUpdate(userPropertyDto);

        return UserPropertyDto.builder()
                .id(id)
                .weight(userProperty.getWeight())
                .height(userProperty.getHeight())
                .bodyFat(userProperty.getBodyFat())
                .age(userProperty.getAge())
                .build();
    }

    @GetMapping("/me/property/{id}")
    public UserPropertyDto getUserProperty(@PathVariable("id") Long id) throws Exception {
        User user = userRepository.findById(id).orElseThrow(() -> new Exception("Request id is not valid."));
        UserProperty userProperty = user.getUserProperty();

        return UserPropertyDto.builder()
                .id(userProperty.getId())
                .age(userProperty.getAge())
                .bodyFat(userProperty.getBodyFat())
                .height(userProperty.getHeight())
                .weight(userProperty.getWeight())
                .build();
    }

    @GetMapping("/me")
    public UserDto getUserInfo(HttpServletRequest request) {
        String email = JwtUtil.findEmailByToken(JwtUtil.resolveAuthToken(request));

        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
        return UserDto.builder()
                .name(user.getName())
                .id(user.getId())
                .email(user.getEmail())
                .image(user.getImage())
                .build();
    }
}

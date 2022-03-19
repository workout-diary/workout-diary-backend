package com.deukgeun.workout.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OAuth2User {

    private Long id;
    private String name;
    private String email;
    private String image;
}

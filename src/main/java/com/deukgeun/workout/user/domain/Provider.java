package com.deukgeun.workout.user.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Provider {
    KAKAO("KAKAO"),
    GOOGLE("GOOGLE");

    private final String provider;
}

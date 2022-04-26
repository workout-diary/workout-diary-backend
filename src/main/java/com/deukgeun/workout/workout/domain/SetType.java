package com.deukgeun.workout.workout.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SetType {
    NORMAL("NORMAL"),
    SUPER("SUPER"),
    PYRAMID("PYRAMID"),
    COMPOUND("COMPOUND"),
    DROP("DROP");

    private final String setType;
}

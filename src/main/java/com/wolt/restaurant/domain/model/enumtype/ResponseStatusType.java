package com.wolt.restaurant.domain.model.enumtype;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ResponseStatusType {
    SUCCESS("success"),
    FAILURE("failure");

    private final String value;
}
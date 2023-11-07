package com.wanted.teamV.type;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum RestaurantType {
    FAST_FOOD("패스트푸드"),
    CHINESE("중국식"),
    LUNCH_BOX("김밥(도시락)"),
    JAPANESE("일식"),
    EMPTY("없음")
    ;

    private final String type;

    RestaurantType(String type) {
        this.type = type;
    }

    public static RestaurantType toEnum(String target) {
        return Arrays.stream(values())
                .filter(type -> type.isSameType(target))
                .findFirst()
                .orElse(EMPTY);
    }

    private boolean isSameType(String type) {
        return this.type.equalsIgnoreCase(type);
    }
}

package com.wanted.teamV.type;

import com.wanted.teamV.exception.CustomException;
import com.wanted.teamV.exception.ErrorCode;

import java.util.Arrays;

public enum RestaurantOrdering {
    DISTANCE, //거리순
    RATING; //평점순

    public static RestaurantOrdering toEnum(String target) {
        return Arrays.stream(values())
                .filter(type -> type.isSameType(target))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_RESTAURANT_SORT_TYPE));
    }

    private boolean isSameType(String type) {
        return this.name().equalsIgnoreCase(type);
    }

    public boolean isOrderByRating() {
        return this.equals(RestaurantOrdering.RATING);
    }
}

package com.wanted.teamV.dto.res;

import com.wanted.teamV.entity.Rating;

public record RatingResDto(
        Long userId,
        Long restaurantId,
        int score,
        String content
) {
    public RatingResDto(Rating rating) {
        this(
                rating.getMemberId(),
                rating.getRestaurantId(),
                rating.getScore(),
                rating.getContent()
        );
    }
}

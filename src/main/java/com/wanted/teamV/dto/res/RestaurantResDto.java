package com.wanted.teamV.dto.res;

import com.wanted.teamV.entity.Restaurant;

public record RestaurantResDto(
        String name,
        String roadnameAddress,
        String zipCode,
        double rating,
        double distance
) {
    public RestaurantResDto(Restaurant restaurant, double distance) {
        this(
                restaurant.getName(),
                restaurant.getRoadnameAddress(),
                restaurant.getZipCode(),
                restaurant.getAverageRating(),
                distance
        );
    }
}

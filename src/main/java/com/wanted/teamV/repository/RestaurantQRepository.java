package com.wanted.teamV.repository;

import com.wanted.teamV.entity.Restaurant;
import com.wanted.teamV.type.RestaurantOrdering;
import com.wanted.teamV.type.RestaurantType;

import java.util.List;

public interface RestaurantQRepository {
    List<Restaurant> getRestaurants(double lat, double lon, double range, RestaurantOrdering ordering, int page, String restaurantName, RestaurantType restaurantType);

    List<Restaurant> findRecommendRestaurants(Double lat, Double lon);
}

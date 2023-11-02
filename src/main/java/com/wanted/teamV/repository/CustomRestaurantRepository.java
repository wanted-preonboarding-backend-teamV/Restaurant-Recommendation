package com.wanted.teamV.repository;

import com.wanted.teamV.entity.Restaurant;
import com.wanted.teamV.type.RestaurantType;

import java.util.List;

public interface CustomRestaurantRepository {
    List<Restaurant> getRestaurants(int page, String restaurantName, RestaurantType restaurantType);
}

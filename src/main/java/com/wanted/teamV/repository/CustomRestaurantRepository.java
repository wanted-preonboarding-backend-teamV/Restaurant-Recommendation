package com.wanted.teamV.repository;

import com.wanted.teamV.entity.Restaurant;

import java.util.List;

public interface CustomRestaurantRepository {
    List<Restaurant> findRestaurants(int page, String restaurantName, String restaurantType);
}

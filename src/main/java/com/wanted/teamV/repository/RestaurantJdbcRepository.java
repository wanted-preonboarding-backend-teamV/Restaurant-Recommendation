package com.wanted.teamV.repository;

import com.wanted.teamV.entity.Restaurant;

import java.util.List;

public interface RestaurantJdbcRepository {
    void bulkInsert(List<Restaurant> restaurants);
}

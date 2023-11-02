package com.wanted.teamV.service;

import com.wanted.teamV.dto.res.RestaurantDetailResDto;
import com.wanted.teamV.dto.res.RestaurantDistrictResDto;
import com.wanted.teamV.dto.res.RestaurantResDto;

import java.util.List;

public interface RestaurantService {
    List<RestaurantDistrictResDto> getDistricts();
    List<RestaurantResDto> getRestaurants(
            String lat,
            String lon,
            double range,
            String order,
            int page,
            String restaurantName,
            String restaurantType
    );
    RestaurantDetailResDto getRestaurant(Long restaurantId);
}

package com.wanted.teamV.service.impl;

import com.wanted.teamV.dto.res.RatingResDto;
import com.wanted.teamV.dto.res.RestaurantDetailResDto;
import com.wanted.teamV.dto.res.RestaurantDistrictResDto;
import com.wanted.teamV.dto.res.RestaurantResDto;
import com.wanted.teamV.entity.Rating;
import com.wanted.teamV.entity.Restaurant;
import com.wanted.teamV.repository.RestaurantRepository;
import com.wanted.teamV.service.RestaurantService;
import com.wanted.teamV.type.RestaurantOrdering;
import com.wanted.teamV.type.RestaurantType;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {
    private final FileParser fileParser;
    private final RestaurantRepository restaurantRepository;

    @Override
    @Cacheable(value = "districts", key = "'sggList'", cacheManager = "cacheManager")
    public List<RestaurantDistrictResDto> getDistricts() {
        return fileParser.parse().stream()
                .map(RestaurantDistrictResDto::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RestaurantResDto> getRestaurants(String lat, String lon, double range, String order, int page, String restaurantName, String restaurantType) {
        double memberLat = Double.parseDouble(lat);
        double memberLon = Double.parseDouble(lon);
        RestaurantType type = RestaurantType.toEnum(restaurantType);
        RestaurantOrdering ordering = RestaurantOrdering.toEnum(order);

        return restaurantRepository.getRestaurants(memberLat, memberLon, range, ordering, page, restaurantName, type).stream()
                .map(restaurant -> new RestaurantResDto(restaurant, DistanceCalculator.calculate(memberLat, memberLon, restaurant.getLat(), restaurant.getLon())))
                .toList();
    }

    @Override
    @Cacheable(value = "restaurant", key = "#restaurantId", cacheManager = "cacheManager")
    public RestaurantDetailResDto getRestaurant(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.getById(restaurantId);

        return restaurant.getRatingList().stream()
                .sorted(Comparator.comparing(Rating::getCreatedAt).reversed())
                .map(RatingResDto::new)
                .collect(collectingAndThen(toList(), ratings -> new RestaurantDetailResDto(restaurant, ratings)));
    }
}
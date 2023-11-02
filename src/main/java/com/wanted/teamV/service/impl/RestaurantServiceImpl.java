package com.wanted.teamV.service.impl;

import com.wanted.teamV.dto.Coordinate;
import com.wanted.teamV.dto.res.RatingResDto;
import com.wanted.teamV.dto.res.RestaurantDetailResDto;
import com.wanted.teamV.dto.res.RestaurantDistrictResDto;
import com.wanted.teamV.dto.res.RestaurantResDto;
import com.wanted.teamV.entity.Rating;
import com.wanted.teamV.entity.Restaurant;
import com.wanted.teamV.repository.RestaurantQRepository;
import com.wanted.teamV.repository.RestaurantRepository;
import com.wanted.teamV.service.RestaurantService;
import com.wanted.teamV.type.RestaurantOrdering;
import com.wanted.teamV.type.RestaurantType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {
    private final FileParser fileParser;
    private final RestaurantQRepository restaurantQRepository;
    private final RestaurantRepository restaurantRepository;

    @Override
    public List<RestaurantDistrictResDto> getDistricts() {
        return fileParser.parse().stream()
                .map(RestaurantDistrictResDto::toDto)
                .toList();
    }

    @Override
    public List<RestaurantResDto> getRestaurants(String lat, String lon, double range, String order, int page, String restaurantName, String restaurantType) {
        Coordinate coordinate = new Coordinate(Double.parseDouble(lat), Double.parseDouble(lon));
        RestaurantType type = RestaurantType.toEnum(restaurantType);
        Map<Restaurant, Double> restaurants = new HashMap<>();

        //요청한 좌표 안에 있는 맛집 필터링
        restaurantQRepository.getRestaurants(page, restaurantName, type).forEach(restaurant -> {
                    Coordinate restaurantCoordinate = new Coordinate(restaurant.getLat(), restaurant.getLon());
                    double distance = DistanceCalculator.calculate(coordinate, restaurantCoordinate); //km

                    if(distance <= range) {
                        restaurants.put(restaurant, distance);
                    }
                });

        List<Restaurant> keySet = new ArrayList<>(restaurants.keySet());
        RestaurantOrdering ordering = RestaurantOrdering.toEnum(order);

        if(ordering.isOrderByDistance()) {
            keySet.sort(Comparator.comparing(restaurants::get));

            return keySet.stream()
                    .map(restaurant -> new RestaurantResDto(restaurant, restaurants.get(restaurant)))
                    .toList();
        }

        return keySet.stream()
                .sorted(Comparator.comparing(Restaurant::getAverageRating))
                .map(restaurant -> new RestaurantResDto(restaurant, restaurants.get(restaurant)))
                .toList();
    }

    @Override
    public RestaurantDetailResDto getRestaurant(Long restaurantId) {
        Restaurant restaurant = restaurantRepository.getById(restaurantId);

        return restaurant.getRatingList().stream()
                .sorted(Comparator.comparing(Rating::getCreatedAt).reversed())
                .map(RatingResDto::new)
                .collect(collectingAndThen(toList(), ratings -> new RestaurantDetailResDto(restaurant, ratings)));
    }
}

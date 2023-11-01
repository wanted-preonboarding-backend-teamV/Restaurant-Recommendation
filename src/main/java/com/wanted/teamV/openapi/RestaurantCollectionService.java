package com.wanted.teamV.openapi;

import com.wanted.teamV.entity.Restaurant;
import com.wanted.teamV.openapi.converter.OpenApiRawRestaurant;
import com.wanted.teamV.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantCollectionService {

    private final OpenApiService openApiService;
    private final RestaurantRepository restaurantRepository;

    @Transactional
    public void collectAllRestaurants() {
        for (OpenApiRestaurantType restaurantType : OpenApiRestaurantType.values()) {
            log.info("Collect restaurants ({})", restaurantType.getType());
            List<OpenApiRawRestaurant> rawRestaurants = openApiService.getRawDataFromOpenapi(restaurantType);
            List<OpenApiRawRestaurant> preprocessed = openApiService.preprocessRawData(rawRestaurants, restaurantType);
            List<Restaurant> restaurants = openApiService.rawToRestaurants(preprocessed);

            List<Restaurant> restaurantsForSave = restaurants.stream().filter(it -> {
                List<Restaurant> restaurantOptional = restaurantRepository.findAllByNameAndRoadnameAddress(
                        it.getName(), it.getRoadnameAddress());
                if (!restaurantOptional.isEmpty()) {
                    log.warn("Duplicate restaurant: {} ({})", it.getName(), it.getRoadnameAddress());
                    return false;
                } else {
                    return true;
                }
            }).toList();

            restaurantRepository.saveAll(restaurantsForSave);
            log.info("Saving {} restaurants ({}) is done.", restaurantsForSave.size(), restaurantType.getType());
        }
    }

}

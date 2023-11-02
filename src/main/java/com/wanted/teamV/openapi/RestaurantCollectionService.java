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
        int newCount = 0, duplicatedCount = 0;

        for (OpenApiRestaurantType restaurantType : OpenApiRestaurantType.values()) {
            log.info("Collect restaurants ({})", restaurantType.getType());
            List<OpenApiRawRestaurant> rawRestaurants = openApiService.getRawDataFromOpenapi(restaurantType);
            List<OpenApiRawRestaurant> preprocessed = openApiService.preprocessRawData(rawRestaurants, restaurantType);
            List<Restaurant> restaurants = openApiService.rawToRestaurants(preprocessed);

            List<Restaurant> restaurantsForSave = restaurants.stream().filter(it ->
                restaurantRepository.findAllByNameAndRoadnameAddress(it.getName(), it.getRoadnameAddress())
                        .isEmpty()
            ).toList();
            newCount += restaurantsForSave.size();
            duplicatedCount += (restaurants.size() - restaurantsForSave.size());

            restaurantRepository.bulkInsert(restaurantsForSave);
            log.info("Saving {} restaurants ({}) is done.", restaurantsForSave.size(), restaurantType.getType());
        }

        log.info("All Restaurants are saved. (new: {}, duplicated: {})", newCount, duplicatedCount);
    }

}

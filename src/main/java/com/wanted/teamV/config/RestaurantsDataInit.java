package com.wanted.teamV.config;

import com.wanted.teamV.openapi.RestaurantCollectionService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("test-data")
@Component
@RequiredArgsConstructor
public class RestaurantsDataInit {
    private final RestaurantCollectionService collectionService;

    @PostConstruct
    public void init() {
        collectionService.collectAllRestaurants();
    }
}

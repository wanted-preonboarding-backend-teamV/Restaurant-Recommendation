package com.wanted.teamV.openapi;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface OpenApi {
    @GetExchange("/{restaurantType}")
    String getRestaurantsInfo(
            @RequestParam("key") String serviceKey,
            @RequestParam("type") String type,
            @RequestParam("pIndex") int page,
            @RequestParam("pSize") int size,
            @PathVariable("restaurantType") String restaurantType
    );
}

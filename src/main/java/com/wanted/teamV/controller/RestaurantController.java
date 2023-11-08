package com.wanted.teamV.controller;

import com.wanted.teamV.dto.res.RestaurantDetailResDto;
import com.wanted.teamV.dto.res.RestaurantDistrictResDto;
import com.wanted.teamV.dto.res.RestaurantResDto;
import com.wanted.teamV.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/restaurants")
public class RestaurantController {
    private final RestaurantService restaurantService;

    @GetMapping("/district")
    public ResponseEntity<List<RestaurantDistrictResDto>> getDistricts() {
        List<RestaurantDistrictResDto> response = restaurantService.getDistricts();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<RestaurantResDto>> getRestaurants(
            @RequestParam("lat") String lat,
            @RequestParam("lon") String lon,
            @RequestParam("range") double range,
            @RequestParam(defaultValue = "distance") String order,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(value = "search", defaultValue = "") String restaurantName,
            @RequestParam(value = "filter", defaultValue = "") String restaurantType
    ) {
        List<RestaurantResDto> response = restaurantService.getRestaurants(lat, lon, range, order, page, restaurantName, restaurantType);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDetailResDto> getRestaurant(
            @PathVariable("id") Long restaurantId
    ) {
        RestaurantDetailResDto response = restaurantService.getRestaurant(restaurantId);
        return ResponseEntity.ok(response);
    }
}

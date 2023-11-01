package com.wanted.teamV.controller;

import com.wanted.teamV.dto.res.RestaurantDistrictResDto;
import com.wanted.teamV.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/restaurants")
public class RestaurantController {
    private final RestaurantService restaurantService;

    @GetMapping("/district")
    public ResponseEntity<List<RestaurantDistrictResDto>> getDistricts() {
        List<RestaurantDistrictResDto> districts = restaurantService.getDistricts();
        return ResponseEntity.ok(districts);
    }
}

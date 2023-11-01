package com.wanted.teamV.service;

import com.wanted.teamV.dto.res.RestaurantDistrictResDto;

import java.util.List;

public interface RestaurantService {
    List<RestaurantDistrictResDto> getDistricts();
}

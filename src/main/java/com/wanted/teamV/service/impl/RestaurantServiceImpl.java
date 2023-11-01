package com.wanted.teamV.service.impl;

import com.wanted.teamV.dto.res.RestaurantDistrictResDto;
import com.wanted.teamV.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {
    private final FileParser fileParser;

    @Override
    public List<RestaurantDistrictResDto> getDistricts() {
        return fileParser.parse().stream()
                .map(RestaurantDistrictResDto::toDto)
                .toList();
    }
}

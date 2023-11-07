package com.wanted.teamV.service;

import com.wanted.teamV.entity.District;

import java.util.Optional;

public interface DistrictRedisService {
    District save(District district);

    Optional<District> findById(String Id);
}

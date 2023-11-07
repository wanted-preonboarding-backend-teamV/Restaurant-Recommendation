package com.wanted.teamV.service.impl;

import com.wanted.teamV.entity.District;
import com.wanted.teamV.repository.DistrictRedisRepository;
import com.wanted.teamV.service.DistrictRedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DistrictRedisServiceImpl implements DistrictRedisService {

    private final DistrictRedisRepository districtRedisRepository;

    @Override
    public District save(District district) {
        return districtRedisRepository.save(district);
    }

    @Override
    public Optional<District> findById(String Id) {
        return districtRedisRepository.findById(Id);
    }
}

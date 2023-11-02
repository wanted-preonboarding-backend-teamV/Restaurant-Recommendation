package com.wanted.teamV.repository;

import com.wanted.teamV.entity.Restaurant;
import com.wanted.teamV.exception.CustomException;
import com.wanted.teamV.exception.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    default Restaurant getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
    }
}

package com.wanted.teamV.repository;

import com.wanted.teamV.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    List<Restaurant> findAllByNameAndRoadnameAddress(String name, String roadnameAddress);
}

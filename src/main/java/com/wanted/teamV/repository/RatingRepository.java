package com.wanted.teamV.repository;

import com.wanted.teamV.entity.Rating;
import com.wanted.teamV.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    void deleteAllByRestaurant(Restaurant restaurant);
}

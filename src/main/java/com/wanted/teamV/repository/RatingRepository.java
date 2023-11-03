package com.wanted.teamV.repository;

import com.wanted.teamV.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    @Query(value = "SELECT AVG(r.score) FROM Rating r WHERE r.restaurant_id = :restaurantId", nativeQuery = true)
    Double calculateAverageRatingByRestaurant(@Param("restaurantId") Long restaurantId);

}

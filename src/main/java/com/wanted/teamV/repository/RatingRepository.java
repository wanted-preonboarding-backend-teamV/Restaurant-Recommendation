package com.wanted.teamV.repository;

import com.wanted.teamV.entity.Rating;
import com.wanted.teamV.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    @Query("SELECT AVG(r.score) FROM Rating r WHERE r.restaurant.id = :restaurantId")
    Double calculateAverageRatingByRestaurant(@Param("restaurantId") Long restaurantId);

    void deleteAllByRestaurant(Restaurant restaurant);

}

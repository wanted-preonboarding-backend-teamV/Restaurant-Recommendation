package com.wanted.teamV.repository;

import com.wanted.teamV.entity.Restaurant;
import com.wanted.teamV.exception.CustomException;
import com.wanted.teamV.exception.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long>, RestaurantJdbcRepository {
    List<Restaurant> findAllByNameAndRoadnameAddress(String name, String roadnameAddress);

    @Query("SELECT r FROM Restaurant r join fetch r.ratingList WHERE r.id = :id")
    Optional<Restaurant> findByIdWithRatingsUsingFetchJoin(@Param("id") Long id);

    default Restaurant getById(Long id) {
        return findByIdWithRatingsUsingFetchJoin(id)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
    }
}

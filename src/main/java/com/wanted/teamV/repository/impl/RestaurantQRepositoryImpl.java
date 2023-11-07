package com.wanted.teamV.repository.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wanted.teamV.entity.Restaurant;
import com.wanted.teamV.repository.RestaurantQRepository;
import com.wanted.teamV.type.RestaurantType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.wanted.teamV.entity.QRestaurant.restaurant;
import static com.wanted.teamV.type.RestaurantType.*;

@Component
@RequiredArgsConstructor
public class RestaurantQRepositoryImpl implements RestaurantQRepository {
    private static final int PAGE_SIZE = 10;
    private final JPAQueryFactory queryFactory;
    private final List<String> RESTAURANT_TYPE = new ArrayList<>(
            Arrays.stream(RestaurantType.values())
                    .map(RestaurantType::getType)
                    .filter(type -> !type.equals("없음"))
                    .collect(Collectors.toList())
    );

    @Override
    public List<Restaurant> getRestaurants(int page, String restaurantName, RestaurantType restaurantType) {
        return queryFactory
                .selectFrom(restaurant)
                .where(restaurant.name.containsIgnoreCase(restaurantName).or(filterRestaurantType(restaurantType)))
                .offset(page)
                .limit(PAGE_SIZE)
                .fetch();
    }

    private BooleanExpression filterRestaurantType(RestaurantType type) {
        return switch (type) {
            case FAST_FOOD -> restaurant.sanittnIndutypeNm.eq(FAST_FOOD.getType());
            case LUNCH_BOX -> restaurant.sanittnIndutypeNm.eq(LUNCH_BOX.getType());
            case CHINESE -> restaurant.sanittnIndutypeNm.eq(CHINESE.getType());
            case JAPANESE -> restaurant.sanittnIndutypeNm.eq(JAPANESE.getType());
            case EMPTY -> null;
        };
    }

    // 사용자로부터 거리가 1km 이내인 음식점을 평균 평점이 높은 순서대로 최대 5개 까지 반환
    @Override
    public List<Restaurant> findRecommendRestaurants(Double memberLat, Double memberLon) {
        double distanceInKm = 1; // 1km 근처

        List<Restaurant> recommendedRestaurants = new ArrayList<>();

        for (String type : RESTAURANT_TYPE) {
            List<Restaurant> typeRestaurants = queryFactory
                    .selectFrom(restaurant)
                    .where(restaurant.type.eq(type)
                            .and(haversineDistance(memberLat, memberLon, restaurant.lat, restaurant.lon).loe(distanceInKm))
                    )
                    .orderBy(restaurant.averageRating.desc())
                    .limit(5)
                    .fetch();

            recommendedRestaurants.addAll(typeRestaurants);
        }

        return recommendedRestaurants;
    }

    // Harversine 공식이용하여 사용자와 음식점 거리 계산
    private NumberExpression<Double> haversineDistance(double lat1, double lon1, NumberPath<Double> lat2, NumberPath<Double> lon2) {
        return Expressions.numberTemplate(Double.class,
                "6371 * acos(cos(radians({0})) * cos(radians({2})) * cos(radians({3}) - radians({1})) + sin(radians({0})) * sin(radians({2})))",
                lat1, lon1, lat2, lon2);
    }

}

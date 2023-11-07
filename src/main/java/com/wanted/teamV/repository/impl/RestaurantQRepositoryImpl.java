package com.wanted.teamV.repository.impl;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wanted.teamV.entity.Restaurant;
import com.wanted.teamV.repository.RestaurantQRepository;
import com.wanted.teamV.type.RestaurantOrdering;
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
                    .collect(Collectors.toList())
    );

    @Override
    public List<Restaurant> getRestaurants(double lat, double lon, double range, RestaurantOrdering ordering, int page, String restaurantName, RestaurantType restaurantType) {
        return queryFactory
                .selectFrom(restaurant)
                .where(haversineDistance(lat, lon, restaurant.lat, restaurant.lon).loe(range)
                        .and(restaurant.name.containsIgnoreCase(restaurantName)
                                .or(filterRestaurantType(restaurantType)))
                )
                .offset(page)
                .limit(PAGE_SIZE)
                .orderBy(orderRestaurants(lat, lon, ordering))
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

    private OrderSpecifier<?> orderRestaurants(double lat, double lon, RestaurantOrdering order) {
        if(order.isOrderByRating()) {
            return restaurant.averageRating.desc();
        }

        return haversineDistance(lat, lon, restaurant.lat, restaurant.lon).asc();
    }

    // 사용자로부터 거리가 500미터 이내인 음식점을 평균 평점이 높은 순서대로 최대 5개 까지 반환
    @Override
    public List<Restaurant> findRecommendRestaurants(Double memberLat, Double memberLon) {
        double distanceInKm = 0.5; // 500미터 근처

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
        // latitude 를 radians 로 계산
        NumberExpression<Double> radiansLatitude =
                Expressions.numberTemplate(Double.class, "radians({0})", lat1);

        // 계산된 latitude -> 코사인 계산
        NumberExpression<Double> cosLatitude =
                Expressions.numberTemplate(Double.class, "cos({0})", radiansLatitude);
        NumberExpression<Double> cosRestaurantLatitude =
                Expressions.numberTemplate(Double.class, "cos(radians({0}))", lat2);

        // 계산된 latitude -> 사인 계산
        NumberExpression<Double> sinLatitude =
                Expressions.numberTemplate(Double.class, "sin({0})", radiansLatitude);
        NumberExpression<Double> sinRestaurantLatitude =
                Expressions.numberTemplate(Double.class, "sin(radians({0}))", lat2);

        // 사이 거리 계산
        NumberExpression<Double> cosLongitude =
                Expressions.numberTemplate(Double.class, "cos(radians({0}) - radians({1}))", lon2, lon1);

        NumberExpression<Double> acosExpression =
                Expressions.numberTemplate(Double.class, "acos({0})",
                        cosLatitude.multiply(cosRestaurantLatitude).multiply(cosLongitude).add(sinLatitude.multiply(sinRestaurantLatitude)));

        // 최종 계산
        NumberExpression<Double> distanceExpression =
                Expressions.numberTemplate(Double.class, "6371 * {0}", acosExpression);

        return distanceExpression;
    }

}

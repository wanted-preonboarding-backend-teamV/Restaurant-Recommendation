package com.wanted.teamV.repository.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wanted.teamV.entity.Restaurant;
import com.wanted.teamV.repository.RestaurantQRepository;
import com.wanted.teamV.type.RestaurantType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.wanted.teamV.entity.QRestaurant.restaurant;
import static com.wanted.teamV.type.RestaurantType.*;

@Component
@RequiredArgsConstructor
public class RestaurantQRepositoryImpl implements RestaurantQRepository {
    private static final int PAGE_SIZE = 10;
    private final JPAQueryFactory queryFactory;

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
}

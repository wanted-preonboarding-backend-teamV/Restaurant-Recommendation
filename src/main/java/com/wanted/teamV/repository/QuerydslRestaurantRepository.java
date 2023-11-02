package com.wanted.teamV.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wanted.teamV.entity.Restaurant;
import com.wanted.teamV.type.RestaurantType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.wanted.teamV.entity.QRestaurant.restaurant;

@Component
@RequiredArgsConstructor
public class QuerydslRestaurantRepository implements CustomRestaurantRepository {
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
            case FAST_FOOD -> restaurant.sanittnIndutypeNm.eq("패스트푸드");
            case LUNCH_BOX -> restaurant.sanittnIndutypeNm.eq("김밥(도시락)");
            case CHINESE -> restaurant.sanittnIndutypeNm.eq("중국식");
            case JAPANESE -> restaurant.sanittnIndutypeNm.eq("일식");
        };
    }
}

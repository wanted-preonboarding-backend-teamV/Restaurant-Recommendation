package com.wanted.teamV.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wanted.teamV.entity.Restaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.wanted.teamV.entity.QRestaurant.restaurant;

@Component
@RequiredArgsConstructor
public class QuerydslRestaurantRepository implements  CustomRestaurantRepository {
    private static final int PAGE_SIZE = 10;
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Restaurant> findRestaurants(int page, String restaurantName, String restaurantType) {
        return queryFactory
                .selectFrom(restaurant)
                .where(restaurant.name.containsIgnoreCase(restaurantName).or(filterRestaurantType(restaurantType)))
                .offset(page)
                .limit(PAGE_SIZE)
                .fetch();
    }

    private BooleanExpression filterRestaurantType(String name) {
        return switch (name) {
            case "패스트푸드" -> restaurant.sanittnIndutypeNm.eq("패스트푸드");
            case "김밥(도시락)" -> restaurant.sanittnIndutypeNm.eq("김밥(도시락)");
            case "중국식" -> restaurant.sanittnIndutypeNm.eq("중국식");
            case "일식" -> restaurant.sanittnIndutypeNm.eq("일식");
            default -> null;
        };
    }
}

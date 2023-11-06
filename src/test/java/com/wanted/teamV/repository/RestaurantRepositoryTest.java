package com.wanted.teamV.repository;

import com.wanted.teamV.config.QuerydslConfig;
import com.wanted.teamV.entity.Restaurant;
import com.wanted.teamV.exception.CustomException;
import com.wanted.teamV.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Import({QuerydslConfig.class})
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class RestaurantRepositoryTest {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("맛집 평균 평점 업데이트 테스트")
    void test_update_average_rating() {
        //given
        double averageRating = 3.5;

        Restaurant restaurant = Restaurant.builder()
                .name("맛집1")
                .sigun("서울시")
                .type("패스트푸드")
                .roadnameAddress("서울특별시 송파구 어쩌구")
                .lotAddress("서울특별시 송파구 어쩌구")
                .zipCode("123456")
                .lat(18.1234567)
                .lon(128.1234567)
                .averageRating(0.0)
                .build();

        entityManager.persistAndFlush(restaurant);

        //when
        restaurantRepository.updateAverageRating(restaurant.getId(), averageRating);
        Restaurant result = restaurantRepository.findById(restaurant.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.RESTAURANT_NOT_FOUND));

        //then
        assertEquals(averageRating, result.getAverageRating());
    }

}

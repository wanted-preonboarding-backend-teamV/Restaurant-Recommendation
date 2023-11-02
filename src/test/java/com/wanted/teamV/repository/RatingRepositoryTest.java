package com.wanted.teamV.repository;

import com.wanted.teamV.entity.Member;
import com.wanted.teamV.entity.Rating;
import com.wanted.teamV.entity.Restaurant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class RatingRepositoryTest {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("맛집 평균 평점 계산 테스트")
    void test_calculate_average_rating_by_restaurant() {
        //given
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

        Member member = Member.builder()
                .account("member1")
                .password("member123!!")
                .lat(0.0)
                .lon(0.0)
                .build();
        entityManager.persistAndFlush(member);

        Rating rating1 = Rating.builder()
                .restaurant(restaurant)
                .member(member)
                .score(4)
                .content("맛있어요!")
                .createdAt(LocalDateTime.now())
                .build();
        entityManager.persistAndFlush(rating1);

        Rating rating2 = Rating.builder()
                .restaurant(restaurant)
                .member(member)
                .score(5)
                .content("맛있어요!")
                .createdAt(LocalDateTime.now())
                .build();
        entityManager.persistAndFlush(rating2);

        //when
        Double averageRating = ratingRepository.calculateAverageRatingByRestaurant(restaurant.getId());

        //then
        assertNotNull(averageRating, "averageRating 값은 null일 수 없습니다.");
        assertEquals(4.5, averageRating);
    }

}

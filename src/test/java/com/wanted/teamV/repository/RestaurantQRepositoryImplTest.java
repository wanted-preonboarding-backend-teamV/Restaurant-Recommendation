package com.wanted.teamV.repository;

import com.wanted.teamV.config.QuerydslConfig;
import com.wanted.teamV.entity.Restaurant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Import({QuerydslConfig.class})
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class RestaurantQRepositoryImplTest {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private TestEntityManager entityManager;

    private List<Restaurant> restaurants;

    // 패스트푸드 음식점 개수
    private long fastFoodCount = 0;

    // 중국식 음식점 개수
    private long chineseFoodCount = 0;

    // 김밥(도시락) 음식점 개수
    private long gimbapCount = 0;

    // 일식 음식점 개수
    private long japaneseFoodCount = 0;

    @Test
    @DisplayName("추천 식당 조회 테스트")
    void find_recommendation_restaurants_test() {
        //given
        double initialLat = 37.1234567; // 초기 lat 값
        double initialLon = 127.1234567; // 초기 lon 값

        for (int i = 1; i <= 5; i++) {
            Restaurant restaurant = Restaurant.builder()
                    .name("Fast Food " + i)
                    .sigun("서울시")
                    .type("패스트푸드")
                    .roadnameAddress("서울특별시 송파구 어쩌구 " + i)
                    .lotAddress("서울특별시 송파구 어쩌구 " + i)
                    .zipCode("123456")
                    .lat(initialLat + (i * 0.001))
                    .lon(initialLon + (i * 0.001))
                    .averageRating(Math.floor(Math.random() * 51) / 10.0)
                    .build();

            entityManager.persistAndFlush(restaurant);
        }

        for (int i = 1; i <= 5; i++) {
            Restaurant restaurant = Restaurant.builder()
                    .name("중국집 " + i)
                    .sigun("서울시")
                    .type("중국식")
                    .roadnameAddress("서울특별시 송파구 어쩌구 " + i)
                    .lotAddress("서울특별시 송파구 어쩌구 " + i)
                    .zipCode("123456")
                    .lat(initialLat + (i * 0.001))
                    .lon(initialLon + (i * 0.0015))
                    .averageRating(Math.floor(Math.random() * 51) / 10.0)
                    .build();

            entityManager.persistAndFlush(restaurant);
        }

        for (int i = 1; i <= 5; i++) {
            Restaurant restaurant = Restaurant.builder()
                    .name("김밥집 " + i)
                    .sigun("서울시")
                    .type("김밥(도시락)")
                    .roadnameAddress("서울특별시 송파구 어쩌구 " + i)
                    .lotAddress("서울특별시 송파구 어쩌구 " + i)
                    .zipCode("123456")
                    .lat(initialLat + (i * 0.0015))
                    .lon(initialLon + (i * 0.001))
                    .averageRating(Math.floor(Math.random() * 51) / 10.0)
                    .build();

            entityManager.persistAndFlush(restaurant);
        }

        for (int i = 1; i <= 5; i++) {
            Restaurant restaurant = Restaurant.builder()
                    .name("일식집 " + i)
                    .sigun("서울시")
                    .type("일식")
                    .roadnameAddress("서울특별시 송파구 어쩌구 " + i)
                    .lotAddress("서울특별시 송파구 어쩌구 " + i)
                    .zipCode("123456")
                    .lat(initialLat + (i * 0.001))
                    .lon(initialLon)
                    .averageRating(Math.floor(Math.random() * 51) / 10.0)
                    .build();

            entityManager.persistAndFlush(restaurant);
        }

        //when
        restaurants = restaurantRepository.findRecommendRestaurants(initialLat, initialLon);

        //then
        // 패스트푸드 음식점 개수 확인
        fastFoodCount = restaurants.stream().filter(restaurant -> "패스트푸드".equals(restaurant.getType())).count();
        assertEquals(5, fastFoodCount);

        // 중국식 음식점 개수 확인
        chineseFoodCount = restaurants.stream().filter(restaurant -> "중국식".equals(restaurant.getType())).count();
        assertEquals(5, chineseFoodCount);

        // 김밥(도시락) 음식점 개수 확인
        gimbapCount = restaurants.stream().filter(restaurant -> "김밥(도시락)".equals(restaurant.getType())).count();
        assertEquals(5, gimbapCount);

        // 일식 음식점 개수 확인
        japaneseFoodCount = restaurants.stream().filter(restaurant -> "일식".equals(restaurant.getType())).count();
        assertEquals(5, japaneseFoodCount);
    }

    @Test
    @DisplayName("추천 식당 조회 및 별점순 정렬 테스트")
    void find_recommendation_restaurants_sorted_by_rating_test() {
        //given
        find_recommendation_restaurants_test();

        //when&then
        restaurants.sort(Comparator.comparing(Restaurant::getAverageRating).reversed());

        // Fast Food 음식점 별점순 정렬 확인
        for (int i = 0; i < fastFoodCount - 1; i++) {
            double rating1 = restaurants.get(i).getAverageRating();
            double rating2 = restaurants.get(i + 1).getAverageRating();
            assertTrue(rating1 >= rating2);
        }

        // 중국집 음식점 별점순 정렬 확인
        for (int i = (int) fastFoodCount; i < fastFoodCount + chineseFoodCount - 1; i++) {
            double rating1 = restaurants.get(i).getAverageRating();
            double rating2 = restaurants.get(i + 1).getAverageRating();
            assertTrue(rating1 >= rating2);
        }

        // 김밥집 음식점 별점순 정렬 확인
        for (int i = (int) (fastFoodCount + chineseFoodCount); i < fastFoodCount + chineseFoodCount + gimbapCount - 1; i++) {
            double rating1 = restaurants.get(i).getAverageRating();
            double rating2 = restaurants.get(i + 1).getAverageRating();
            assertTrue(rating1 >= rating2);
        }

        // 일식집 음식점 별점순 정렬 확인
        for (int i = (int) (fastFoodCount + chineseFoodCount + gimbapCount); i < restaurants.size() - 1; i++) {
            double rating1 = restaurants.get(i).getAverageRating();
            double rating2 = restaurants.get(i + 1).getAverageRating();
            assertTrue(rating1 >= rating2);
        }
    }

}
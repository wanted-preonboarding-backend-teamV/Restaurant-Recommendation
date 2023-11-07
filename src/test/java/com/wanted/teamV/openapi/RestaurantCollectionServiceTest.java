package com.wanted.teamV.openapi;

import com.wanted.teamV.entity.Restaurant;
import com.wanted.teamV.repository.RatingRepository;
import com.wanted.teamV.repository.RestaurantRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;

@Transactional
@SpringBootTest
class RestaurantCollectionServiceTest {

    @Mock
    private OpenApiService apiService = mock(OpenApiService.class);
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private RatingRepository ratingRepository;

    private RestaurantCollectionService collectionService;

    @BeforeEach
    public void setUp() {
        collectionService = new RestaurantCollectionService(apiService, restaurantRepository, ratingRepository);
    }

    @Test
    @DisplayName("폐업한 식당을 제외하고 새로운 식당 정보를 저장한다.")
    public void closed_restaurant_is_not_saved() {
        // given
        Restaurant restaurant1 = Restaurant.builder()
                .name("teamv김밥")
                .sigun("화성시 ")
                .licenseDe(LocalDateTime.of(2015, 2, 5, 0, 0, 0))
                .bsnStateNm("영업")
                .type("김밥(도시락)")
                .roadnameAddress("경기도 화성시 영통로 59, 1층 112호 (반월동, 현대프라자)")
                .lotAddress("경기도 화성시 반월동 869 현대프라자 112호")
                .zipCode("18378")
                .lon(127.0619018628)
                .lat(37.2351254448)
                .averageRating(0.0)
                .build();
        Restaurant restaurant2 = Restaurant.builder()
                .name("teamv도시락")
                .sigun("화성시 ")
                .licenseDe(LocalDateTime.of(1991, 6, 18, 0, 0, 0))
                .bsnStateNm("폐업")
                .type("김밥(도시락)")
                .roadnameAddress("경기도 화성시 봉담읍 와우안길 48")
                .lotAddress("경기도 화성시 봉담읍 와우리 75-3")
                .zipCode("18321")
                .lon(126.9764329175)
                .lat(37.2160203201)
                .averageRating(0.0)
                .build();

        when(apiService.rawToRestaurants(any())).thenReturn(List.of(restaurant1, restaurant2));

        // when
        collectionService.collectAllRestaurants();

        // then
        List<Restaurant> teamvKB = restaurantRepository.findAllByNameAndRoadnameAddress("teamv김밥", "경기도 화성시 영통로 59, 1층 112호 (반월동, 현대프라자)");
        Assertions.assertFalse(teamvKB.isEmpty());
        List<Restaurant> teamvDSR = restaurantRepository.findAllByNameAndRoadnameAddress("teamv도시락", "경기도 화성시 봉담읍 와우안길 48");
        Assertions.assertTrue(teamvDSR.isEmpty());
    }

    @Test
    @DisplayName("기존에 저장된 식당에 대한 정보가 변경되면 DB에도 업데이트된다.")
    public void update_restaurant_info() {
        // given
        restaurantRepository.save(Restaurant.builder()
                .name("teamv김밥")
                .sigun("화성시 ")
                .licenseDe(LocalDateTime.of(2015, 2, 5, 0, 0, 0))
                .bsnStateNm("영업")
                .type("김밥(도시락)")
                .roadnameAddress("경기도 화성시 영통로 59, 1층 112호 (반월동, 현대프라자)")
                .lotAddress("경기도 화성시 반월동 869 현대프라자 112호")
                .zipCode("18378")
                .lon(127.0619018628)
                .lat(37.2351254448)
                .averageRating(0.0)
                .build());

        Restaurant updateRestaurant = Restaurant.builder()
                .name("teamv김밥")
                .sigun("화성시")
                .licenseDe(LocalDateTime.of(2014, 2, 5, 0, 0, 0))
                .bsnStateNm("영업")
                .type("김밥(도시락)")
                .roadnameAddress("경기도 화성시 영통로 59, 1층 112호 (반월동, 현대프라자)")
                .lotAddress("경기도 화성시 반월동 869 현대프라자 112호")
                .zipCode("18378")
                .lon(127.0619018628)
                .lat(37.2351254448)
                .averageRating(0.0)
                .totEmplyCnt(20)
                .build();

        when(apiService.rawToRestaurants(any())).thenReturn(List.of(updateRestaurant));

        // when
        collectionService.collectAllRestaurants();

        // then
        List<Restaurant> teamvKB = restaurantRepository.findAllByNameAndRoadnameAddress("teamv김밥", "경기도 화성시 영통로 59, 1층 112호 (반월동, 현대프라자)");
        Assertions.assertEquals(1, teamvKB.size());
        Restaurant updated = teamvKB.get(0);
        Assertions.assertEquals("화성시", updated.getSigun());
        Assertions.assertEquals(LocalDateTime.of(2014, 2, 5, 0, 0, 0), updated.getLicenseDe());
        Assertions.assertEquals(20, updated.getTotEmplyCnt());
    }

    @Test
    @DisplayName("기존에 저장된 식당이 폐업하면 DB에서도 삭제된다.")
    public void delete_closed_restaurant() {
        // given
        restaurantRepository.save(Restaurant.builder()
                .name("teamv김밥")
                .sigun("화성시 ")
                .licenseDe(LocalDateTime.of(2015, 2, 5, 0, 0, 0))
                .bsnStateNm("영업")
                .type("김밥(도시락)")
                .roadnameAddress("경기도 화성시 영통로 59, 1층 112호 (반월동, 현대프라자)")
                .lotAddress("경기도 화성시 반월동 869 현대프라자 112호")
                .zipCode("18378")
                .lon(127.0619018628)
                .lat(37.2351254448)
                .averageRating(0.0)
                .build());

        Restaurant updateRestaurant = Restaurant.builder()
                .name("teamv김밥")
                .sigun("화성시")
                .licenseDe(LocalDateTime.of(2014, 2, 5, 0, 0, 0))
                .bsnStateNm("폐업")
                .type("김밥(도시락)")
                .roadnameAddress("경기도 화성시 영통로 59, 1층 112호 (반월동, 현대프라자)")
                .lotAddress("경기도 화성시 반월동 869 현대프라자 112호")
                .zipCode("18378")
                .lon(127.0619018628)
                .lat(37.2351254448)
                .averageRating(0.0)
                .totEmplyCnt(20)
                .build();

        when(apiService.rawToRestaurants(any())).thenReturn(List.of(updateRestaurant));

        // when
        collectionService.collectAllRestaurants();

        // then
        List<Restaurant> teamvKB = restaurantRepository.findAllByNameAndRoadnameAddress("teamv김밥", "경기도 화성시 영통로 59, 1층 112호 (반월동, 현대프라자)");
        Assertions.assertEquals(0, teamvKB.size());
    }
}
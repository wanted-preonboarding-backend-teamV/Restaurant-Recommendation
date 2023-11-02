package com.wanted.teamV.openapi;

import com.wanted.teamV.repository.RestaurantRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class OpenApiServiceTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    RestaurantCollectionService collectionService;

    @Autowired
    RestaurantRepository restaurantRepository;

    @BeforeEach
    void setUp() {
        entityManager
                .createNativeQuery("CREATE UNIQUE INDEX restaurant_name_addr_index on restaurant " +
                        "(name, roadname_address)")
                .executeUpdate();
        entityManager
                .createNativeQuery("SET NON_KEYWORDS year")
                .executeUpdate();
    }

    @Test
    @DisplayName("OPEN API를 통해 맛집 데이터를 DB에 저장한다.")
    public void save_all_restaurants_from_openapi() {
        collectionService.collectAllRestaurants();
        long restaurantCnt = restaurantRepository.count();
        Assertions.assertTrue(restaurantCnt > 0);
    }
}
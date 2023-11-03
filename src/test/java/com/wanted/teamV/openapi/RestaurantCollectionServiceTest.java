package com.wanted.teamV.openapi;

import com.wanted.teamV.repository.RestaurantRepository;
import jakarta.persistence.EntityManager;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Transactional
@SpringBootTest(properties = {"schedules.cron.restaurant-collection=0/10 * * * * *"})
class RestaurantCollectionServiceTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    RestaurantRepository restaurantRepository;

    @BeforeEach
    public void setUp() {
        entityManager
                .createNativeQuery("CREATE UNIQUE INDEX restaurant_name_addr_index on restaurant " +
                        "(name, roadname_address)")
                .executeUpdate();
    }

    @Test
    public void schedule_test() {
        long beforeCnt = restaurantRepository.count();
        Assertions.assertEquals(0, beforeCnt);

        Awaitility.await()
                .atMost(3, TimeUnit.MINUTES)     // 최대 3분까지 확인
                .pollDelay(Duration.ofSeconds(20))      // 처음 20초 기다림
                .pollInterval(Duration.ofSeconds(5))    // 5초마다 조건 확인
                .untilAsserted (() -> {
                    long afterCnt = restaurantRepository.count();
                    Assertions.assertTrue(afterCnt > 0);
                });
    }


}
package com.wanted.teamV.openapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wanted.teamV.entity.Restaurant;
import com.wanted.teamV.openapi.converter.OpenApiRawRestaurant;
import com.wanted.teamV.repository.RatingRepository;
import com.wanted.teamV.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantCollectionService {

    private final OpenApiService openApiService;
    private final RestaurantRepository restaurantRepository;
    private final RatingRepository ratingRepository;

    private final String CLOSED_STATE = "폐업";

    /**
     * OpenApi로부터 식당 데이터를 불러와서 신규 식당은 추가하고 기존 식당은 업데이트하는 메소드
     * 매일 새벽 2시에 실행됨
     * 기존 식당이 폐업한 경우에는 DB에서 삭제한다 (리뷰도 모두 삭제)
     */
    @Scheduled(cron = "0 0 2 * * *", zone = "Asia/Seoul")
    public void collectAllRestaurants() {
        log.info("** Schedule task is started: Load & Save Restaurants **");

        for (OpenApiRestaurantType restaurantType : OpenApiRestaurantType.values()) {
            try {
                log.debug("Collect restaurants ({})", restaurantType.getType());

                List<OpenApiRawRestaurant> rawRestaurants = openApiService.getRawDataFromOpenapi(restaurantType);
                List<OpenApiRawRestaurant> preprocessed = openApiService.preprocessRawData(rawRestaurants, restaurantType);
                List<Restaurant> restaurants = openApiService.rawToRestaurants(preprocessed);

                List<Restaurant> newRestaurants = new ArrayList<>();
                for (Restaurant restaurant : restaurants) {
                    List<Restaurant> duplicates = restaurantRepository.findAllByNameAndRoadnameAddress(
                            restaurant.getName(), restaurant.getRoadnameAddress());

                    if (duplicates.isEmpty() && !restaurant.getBsnStateNm().equals(CLOSED_STATE)) {
                        newRestaurants.add(restaurant);
                    }

                    if (!duplicates.isEmpty()) {
                        Restaurant originalRestaurant = duplicates.get(0);
                        if (restaurant.getBsnStateNm().equals(CLOSED_STATE)) {
                            ratingRepository.deleteAllByRestaurant(originalRestaurant);
                            restaurantRepository.delete(originalRestaurant);
                        } else {
                            originalRestaurant.update(restaurant);
                            // @Transactional을 사용하지 않기 때문에 변경감지가 일어나지 않아 강제로 save함
                            restaurantRepository.save(originalRestaurant);
                        }
                    }
                }

                restaurantRepository.bulkInsert(newRestaurants);

                log.debug("Save & Update is done: {}", restaurantType.getType());
            } catch (WebClientResponseException | JsonProcessingException ex) {
                log.error("Cannot save restaurants ({}).", restaurantType.getType(), ex);
            }
        }

        log.info("** Schedule Task is done: Load & Save Restaurants **");
    }

}

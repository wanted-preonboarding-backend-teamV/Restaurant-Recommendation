package com.wanted.teamV.service.averageRating;

import com.wanted.teamV.entity.Rating;
import com.wanted.teamV.exception.CustomException;
import com.wanted.teamV.repository.RatingRepository;
import com.wanted.teamV.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;

import static com.wanted.teamV.exception.ErrorCode.INVALID_RATING_ENTITY;

@Service
@RequiredArgsConstructor
public class RatingBatchService {

    private final RatingRepository ratingRepository;
    private final RestaurantRepository restaurantRepository;
    private final ScoreCalculationQueue scoreCalculationQueue;

    // 30초 마다 Queue에 있는 Rating을 이용하여 Restaurant에 평균 평점을 계산하여 저장하는 메서드
    @Async
    @Scheduled(cron = "${schedules.cron.rating-batch}")
    public void updateAverageRatings() {
        while (!scoreCalculationQueue.isQueueEmpty()) {
            Rating rating = scoreCalculationQueue.dequeueRatingForCalculation();

            // Rating 객체가 null이 아니고 Restaurant 객체도 null이 아닌 경우에만 처리
            if (rating != null && rating.getRestaurant() != null) {
                double averageRating = ratingRepository.calculateAverageRatingByRestaurant(rating.getRestaurant().getId());
                DecimalFormat decimalFormat = new DecimalFormat("#.#");
                averageRating = Double.parseDouble(decimalFormat.format(averageRating));

                restaurantRepository.updateAverageRating(rating.getRestaurant().getId(), averageRating);
            } else {
                throw new CustomException(INVALID_RATING_ENTITY);
            }
        }
    }

}

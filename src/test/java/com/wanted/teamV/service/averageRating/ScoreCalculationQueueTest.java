package com.wanted.teamV.service.averageRating;

import com.wanted.teamV.entity.Member;
import com.wanted.teamV.entity.Rating;
import com.wanted.teamV.entity.Restaurant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ScoreCalculationQueueTest {

    @InjectMocks
    private ScoreCalculationQueue scoreCalculationQueue;

    @Test
    @DisplayName("enqueue 및 dequeue 테스트")
    void enqueue_and_dequeue_test() {
        Rating rating1 = mock(Rating.class);
        Rating rating2 = mock(Rating.class);

        // enqueue
        scoreCalculationQueue.enqueueRatingForCalculation(rating1);
        scoreCalculationQueue.enqueueRatingForCalculation(rating2);

        assertFalse(scoreCalculationQueue.isQueueEmpty());

        // dequeue
        Rating dequeued1 = scoreCalculationQueue.dequeueRatingForCalculation();
        Rating dequeued2 = scoreCalculationQueue.dequeueRatingForCalculation();

        assertTrue(scoreCalculationQueue.isQueueEmpty());

        assertSame(rating1, dequeued1);
        assertSame(rating2, dequeued2);

        assertNull(scoreCalculationQueue.dequeueRatingForCalculation());
    }

    @Test
    @DisplayName("isQueueEmpty 테스트")
    void is_queueEmpty_test() {
        // 큐가 비어있을 때
        assertTrue(scoreCalculationQueue.isQueueEmpty());

        Rating rating = mock(Rating.class);
        scoreCalculationQueue.enqueueRatingForCalculation(rating);

        // 큐가 비어있지 않을 때
        assertFalse(scoreCalculationQueue.isQueueEmpty());
    }

}
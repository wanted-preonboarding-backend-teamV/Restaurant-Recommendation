package com.wanted.teamV.service.averageRating;

import com.wanted.teamV.entity.Rating;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class ScoreCalculationQueue {

    private final BlockingQueue<Rating> queue = new LinkedBlockingQueue<>();

    public void enqueueRatingForCalculation(Rating rating) {
        queue.add(rating);
    }

    public Rating dequeueRatingForCalculation() {
        return queue.poll();
    }

    public boolean isQueueEmpty() {
        return queue.isEmpty();
    }

}

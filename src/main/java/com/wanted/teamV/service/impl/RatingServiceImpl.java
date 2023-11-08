package com.wanted.teamV.service.impl;

import com.wanted.teamV.dto.req.RatingCreateReqDto;
import com.wanted.teamV.entity.Member;
import com.wanted.teamV.entity.Rating;
import com.wanted.teamV.entity.Restaurant;
import com.wanted.teamV.exception.CustomException;
import com.wanted.teamV.repository.MemberRepository;
import com.wanted.teamV.repository.RatingRepository;
import com.wanted.teamV.repository.RestaurantRepository;
import com.wanted.teamV.service.RatingService;
import com.wanted.teamV.service.averageRating.ScoreCalculationQueue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.wanted.teamV.exception.ErrorCode.INVALID_REQUEST;
import static com.wanted.teamV.exception.ErrorCode.RESTAURANT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RestaurantRepository restaurantRepository;
    private final RatingRepository ratingRepository;
    private final MemberRepository memberRepository;
    private final ScoreCalculationQueue scoreCalculationQueue;

    // 맛집 평가 생성 메서드
    @Override
    @Transactional
    public void createRating(Long memberId, RatingCreateReqDto request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(INVALID_REQUEST));
        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                .orElseThrow(() -> new CustomException(RESTAURANT_NOT_FOUND));

        Rating rating = Rating.builder()
                .restaurant(restaurant)
                .member(member)
                .score(request.getScore())
                .content(request.getContent())
                .build();

        ratingRepository.save(rating);

        // 평균 평점 계산을 위해 queue에 rating 정보 저장
        scoreCalculationQueue.enqueueRatingForCalculation(rating);
    }

}
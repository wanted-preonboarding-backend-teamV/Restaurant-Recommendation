package com.wanted.teamV.service;

import com.wanted.teamV.dto.req.RatingCreateReqDto;

public interface RatingService {

    void createRating(Long memberId, RatingCreateReqDto request);

}
package com.wanted.teamV.dto.req;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RatingCreateReqDto {

    private long restaurantId;
    private int score;
    private String content;

}
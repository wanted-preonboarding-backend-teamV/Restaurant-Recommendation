package com.wanted.teamV.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RatingCreateReqDto {

    private long restaurantId;
    private int score;
    private String content;

}
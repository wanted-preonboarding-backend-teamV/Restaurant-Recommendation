package com.wanted.teamV.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RatingCreateReqDto {

    private long restaurantId;
    private int score;
    private String content;

}
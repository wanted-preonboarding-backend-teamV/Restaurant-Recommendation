package com.wanted.teamV.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class District {
    private String dosi;
    private String sgg;
    private Double lat;
    private Double lon;
}

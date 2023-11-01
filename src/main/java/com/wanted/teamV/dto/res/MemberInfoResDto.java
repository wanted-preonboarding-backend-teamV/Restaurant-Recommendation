package com.wanted.teamV.dto.res;

import com.wanted.teamV.entity.Member;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberInfoResDto {
    private Long id;
    private String account;
    private Double lat;
    private Double lon;
    private Boolean recommend;
}

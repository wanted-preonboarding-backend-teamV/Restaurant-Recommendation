package com.wanted.teamV.openapi.converter;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OpenApiRawHead {
    private Integer listTotalCount;
    private String resultCode;
    private String resultMessage;
    private String apiVersion;
}

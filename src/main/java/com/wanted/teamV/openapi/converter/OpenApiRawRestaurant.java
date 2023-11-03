package com.wanted.teamV.openapi.converter;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OpenApiRawRestaurant {
    @JsonProperty("SIGUN_NM")
    private String sigunNm;
    @JsonProperty("SIGUN_CD")
    private String sigunCd;
    @JsonProperty("BIZPLC_NM")
    private String bizplcNm;
    @JsonProperty("LICENSG_DE")
    private String licenseDe;
    @JsonProperty("BSN_STATE_NM")
    private String bsnStateNm;
    @JsonProperty("CLSBIZ_DE")
    private String clsbizDe;
    @JsonProperty("LOCPLC_AR")
    private Double locplcAr;
    @JsonProperty("GRAD_FACLT_DIV_NM")
    private String gradFacltDivNm;
    @JsonProperty("MALE_ENFLPSN_CNT")
    private Integer maleEnflpsnCnt;
    @JsonProperty("YY")
    private Integer yy;
    @JsonProperty("MULTI_USE_BIZESTBL_YN")
    private String multiUseBizestblYn;
    @JsonProperty("GRAD_DIV_NM")
    private String gradDivNm;
    @JsonProperty("TOT_FACLT_SCALE")
    private Double totFacltScale;
    @JsonProperty("FEMALE_ENFLPSN_CNT")
    private Integer femaleEnflpsnCnt;
    @JsonProperty("BSNSITE_CIRCUMFR_DIV_NM")
    private String bsnsiteCircumfrDivNm;
    @JsonProperty("SANITTN_INDUTYPE_NM")
    private String sanittnIndutypeNm;
    @JsonProperty("SANITTN_BIZCOND_NM")
    private String sanittnBizcondNm;
    @JsonProperty("TOT_EMPLY_CNT")
    private Integer totEmplyCnt;
    @JsonProperty("REFINE_ROADNM_ADDR")
    private String refineRoadnmAddr;
    @JsonProperty("REFINE_LOTNO_ADDR")
    String refineLotnoAddr;
    @JsonProperty("REFINE_ZIP_CD")
    private String refineZipCd;
    @JsonProperty("REFINE_WGS84_LAT")
    private String refindWgs84Logt;
    @JsonProperty("REFINE_WGS84_LOGT")
    private String refineWgs84Lat;
}

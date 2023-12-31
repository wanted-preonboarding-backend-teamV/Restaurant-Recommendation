package com.wanted.teamV.dto.res;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.wanted.teamV.entity.Restaurant;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record RestaurantDetailResDto(
        String name,
        String sigun,
        String type,
        String roadnameAddress,
        String lotAddress,
        String zipCode,
        Double lat,
        Double lon,
        Double averageRating,
        String bsnStateNm,
        String sigunCd,

        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        LocalDateTime licenseDe,

        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        LocalDateTime clsbizDe,

        Double locplcAr,
        String gradFacltDivNm,
        Integer maleEnflpsnCnt,
        Integer yy,
        Boolean multiUseBizestblYn,
        String gradDivNm,
        Double totFacltScale,
        Integer femaleEnflpsnCnt,
        String circumfrDivNm,
        String sanittnIndutypeNm,
        Integer totEmplyCnt,
        List<RatingResDto> ratings
) {
    public RestaurantDetailResDto(Restaurant restaurant, List<RatingResDto> ratings) {
        this(
                restaurant.getName(),
                restaurant.getSigun(),
                restaurant.getType(),
                restaurant.getRoadnameAddress(),
                restaurant.getLotAddress(),
                restaurant.getZipCode(),
                restaurant.getLat(),
                restaurant.getLon(),
                restaurant.getAverageRating(),
                restaurant.getBsnStateNm(),
                restaurant.getSigunCd(),
                restaurant.getLicenseDe(),
                restaurant.getClsbizDe(),
                restaurant.getLocplcAr(),
                restaurant.getGradFacltDivNm(),
                restaurant.getMaleEnflpsnCnt(),
                restaurant.getYy(),
                restaurant.getMultiUseBizestblYn(),
                restaurant.getGradDivNm(),
                restaurant.getTotFacltScale(),
                restaurant.getFemaleEnflpsnCnt(),
                restaurant.getCircumfrDivNm(),
                restaurant.getSanittnIndutypeNm(),
                restaurant.getTotEmplyCnt(),
                ratings
        );
    }
}

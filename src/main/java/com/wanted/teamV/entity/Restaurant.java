package com.wanted.teamV.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Restaurant {
    @Id
    @Column(name = "restaurant_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    String sigun;

    @Column(nullable = false)
    String type;

    @Column(nullable = false)
    String roadnameAddress;

    @Column(nullable = false)
    String lotAddress;

    @Column(nullable = false)
    String zipCode;

    @Column(nullable = false)
    Double lat;

    @Column(nullable = false)
    Double lon;

    @Column(nullable = false)
    Double avg_rating;

    @OneToMany(mappedBy = "restaurant")
    List<Rating> ratingList = new ArrayList<>();

    //raw 데이터 저장을 위한 필드 -> ERD 확인
    String bsnStateNm;
    String sigunCd;
    LocalDateTime licenseDe;
    LocalDateTime clsbizDe;
    Double locplcAr;
    String gradFacltDivNm;
    Integer maleEnflpsnCnt;
    Integer year;
    Boolean multiUseBizestblYn;
    String gradDivNm;
    Double totFacltScale;
    Integer femaleEnflpsnCnt;
    String circumfrDivNm;
    String sanittnIndutypeNm;
    Integer totEmplyCnt;

    @Builder
    public Restaurant(String name, String sigun, String type, String roadnameAddress, String lotAddress, String zipCode,
                      Double lat, Double lon, Double avg_rating, List<Rating> ratingList, String bsnStateNm, String sigunCd,
                      LocalDateTime licenseDe, LocalDateTime clsbizDe, Double locplcAr, String gradFacltDivNm,
                      Integer maleEnflpsnCnt, Integer year, Boolean multiUseBizestblYn, String gradDivNm, Double totFacltScale,
                      Integer femaleEnflpsnCnt, String circumfrDivNm, String sanittnIndutypeNm, Integer totEmplyCnt) {
        this.name = name;
        this.sigun = sigun;
        this.type = type;
        this.roadnameAddress = roadnameAddress;
        this.lotAddress = lotAddress;
        this.zipCode = zipCode;
        this.lat = lat;
        this.lon = lon;
        this.avg_rating = avg_rating;
        this.ratingList = ratingList;
        this.bsnStateNm = bsnStateNm;
        this.sigunCd = sigunCd;
        this.licenseDe = licenseDe;
        this.clsbizDe = clsbizDe;
        this.locplcAr = locplcAr;
        this.gradFacltDivNm = gradFacltDivNm;
        this.maleEnflpsnCnt = maleEnflpsnCnt;
        this.year = year;
        this.multiUseBizestblYn = multiUseBizestblYn;
        this.gradDivNm = gradDivNm;
        this.totFacltScale = totFacltScale;
        this.femaleEnflpsnCnt = femaleEnflpsnCnt;
        this.circumfrDivNm = circumfrDivNm;
        this.sanittnIndutypeNm = sanittnIndutypeNm;
        this.totEmplyCnt = totEmplyCnt;
    }
}

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
    Double averageRating;

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
    Integer yy;
    Boolean multiUseBizestblYn;
    String gradDivNm;
    Double totFacltScale;
    Integer femaleEnflpsnCnt;
    String circumfrDivNm;
    String sanittnIndutypeNm;
    Integer totEmplyCnt;

    @Builder
    public Restaurant(String name, String sigun, String type, String roadnameAddress, String lotAddress, String zipCode,
                      Double lat, Double lon, Double averageRating, List<Rating> ratingList, String bsnStateNm, String sigunCd,
                      LocalDateTime licenseDe, LocalDateTime clsbizDe, Double locplcAr, String gradFacltDivNm,
                      Integer maleEnflpsnCnt, Integer yy, Boolean multiUseBizestblYn, String gradDivNm, Double totFacltScale,
                      Integer femaleEnflpsnCnt, String circumfrDivNm, String sanittnIndutypeNm, Integer totEmplyCnt) {
        this.name = name;
        this.sigun = sigun;
        this.type = type;
        this.roadnameAddress = roadnameAddress;
        this.lotAddress = lotAddress;
        this.zipCode = zipCode;
        this.lat = lat;
        this.lon = lon;
        this.averageRating = averageRating;
        this.ratingList = ratingList;
        this.bsnStateNm = bsnStateNm;
        this.sigunCd = sigunCd;
        this.licenseDe = licenseDe;
        this.clsbizDe = clsbizDe;
        this.locplcAr = locplcAr;
        this.gradFacltDivNm = gradFacltDivNm;
        this.maleEnflpsnCnt = maleEnflpsnCnt;
        this.yy = yy;
        this.multiUseBizestblYn = multiUseBizestblYn;
        this.gradDivNm = gradDivNm;
        this.totFacltScale = totFacltScale;
        this.femaleEnflpsnCnt = femaleEnflpsnCnt;
        this.circumfrDivNm = circumfrDivNm;
        this.sanittnIndutypeNm = sanittnIndutypeNm;
        this.totEmplyCnt = totEmplyCnt;
    }

    public void update(Restaurant restaurant) {
        this.sigun = restaurant.sigun == null? this.sigun : restaurant.sigun;
        this.type = restaurant.type == null? this.type : restaurant.type;
        this.lotAddress = restaurant.lotAddress == null? this.lotAddress : restaurant.lotAddress;
        this.zipCode = restaurant.zipCode == null? this.zipCode : restaurant.zipCode;
        this.lat = restaurant.lat == null? this.lat : restaurant.lat;
        this.lon = restaurant.lon == null? this.lon : restaurant.lon;
        this.averageRating = restaurant.averageRating == null? this.averageRating : restaurant.averageRating;
        this.ratingList = restaurant.ratingList == null? this.ratingList : restaurant.ratingList;
        this.bsnStateNm = restaurant.bsnStateNm == null? this.bsnStateNm : restaurant.bsnStateNm;
        this.sigunCd = restaurant.sigunCd == null? this.sigunCd : restaurant.sigunCd;
        this.licenseDe = restaurant.licenseDe == null? this.licenseDe : restaurant.licenseDe;
        this.clsbizDe = restaurant.clsbizDe == null? this.clsbizDe : restaurant.clsbizDe;
        this.locplcAr = restaurant.locplcAr == null? this.locplcAr : restaurant.locplcAr;
        this.gradFacltDivNm = restaurant.gradFacltDivNm == null? this.gradFacltDivNm : restaurant.gradFacltDivNm;
        this.maleEnflpsnCnt = restaurant.maleEnflpsnCnt == null? this.maleEnflpsnCnt : restaurant.maleEnflpsnCnt;
        this.yy = restaurant.yy == null? this.yy : restaurant.yy;
        this.multiUseBizestblYn = restaurant.multiUseBizestblYn == null? this.multiUseBizestblYn : restaurant.multiUseBizestblYn;
        this.gradDivNm = restaurant.gradDivNm == null? this.gradDivNm : restaurant.gradDivNm;
        this.totFacltScale = restaurant.totFacltScale == null? this.totFacltScale : restaurant.totFacltScale;
        this.femaleEnflpsnCnt = restaurant.femaleEnflpsnCnt == null? this.femaleEnflpsnCnt : restaurant.femaleEnflpsnCnt;
        this.circumfrDivNm = restaurant.circumfrDivNm == null? this.circumfrDivNm : restaurant.circumfrDivNm;
        this.sanittnIndutypeNm = restaurant.sanittnIndutypeNm == null? this.sanittnIndutypeNm : restaurant.sanittnIndutypeNm;
        this.totEmplyCnt = restaurant.totEmplyCnt == null? this.totEmplyCnt : restaurant.totEmplyCnt;
    }
}

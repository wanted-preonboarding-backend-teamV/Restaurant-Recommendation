package com.wanted.teamV.service;

import com.wanted.teamV.common.ServiceTest;
import com.wanted.teamV.dto.res.RestaurantDetailResDto;
import com.wanted.teamV.dto.res.RestaurantDistrictResDto;
import com.wanted.teamV.dto.res.RestaurantResDto;
import com.wanted.teamV.entity.Member;
import com.wanted.teamV.entity.Rating;
import com.wanted.teamV.entity.Restaurant;
import com.wanted.teamV.repository.MemberRepository;
import com.wanted.teamV.repository.RatingRepository;
import com.wanted.teamV.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ServiceTest
public class RestaurantServiceImplTest {

    @Autowired
    RestaurantService restaurantService;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    RatingRepository ratingRepository;

    Restaurant restaurant;

    @BeforeEach
    void setRestaurant() {
        //row data들은 테스트에 영향이 없기 때문에 값을 넣지 않았습니다.
        LocalDateTime now = LocalDateTime.now();
        Restaurant newRestaurant = Restaurant.builder()
                .name("이름")
                .sigun("부산시 강서구")
                .type("")
                .roadnameAddress("부산시 강서구 가락대로")
                .lotAddress("")
                .zipCode("819217")
                .lat(124.1231231)
                .lon(31.24321)
                .averageRating(3.4)
                .bsnStateNm("")
                .sigunCd("")
                .licenseDe(now)
                .clsbizDe(now)
                .locplcAr(1.0)
                .gradFacltDivNm("")
                .maleEnflpsnCnt(1)
                .yy(2005)
                .multiUseBizestblYn(false)
                .gradDivNm("")
                .totFacltScale(1.9)
                .femaleEnflpsnCnt(1)
                .circumfrDivNm("")
                .sanittnIndutypeNm("")
                .totEmplyCnt(1)
                .build();
        restaurantRepository.save(newRestaurant);
        restaurant = newRestaurant;
    }

    @Nested
    @DisplayName("시군구 목록을 가져온다.")
    class getDistricts {

        @Test
        @DisplayName("성공")
        void success() {
            //given

            //when
            List<RestaurantDistrictResDto> districts = restaurantService.getDistricts();

            //then
            assertThat(districts).isNotEmpty();
        }
    }

    @Nested
    @DisplayName("맛집목록을 가져온다.")
    class getRestaurants {

        @Test
        @DisplayName("성공")
        void success() {
            //given

            //when
            String lat = "124.1231231";
            String lon = "31.24321";
            double range = 1.0;
            String order = "distance";
            int page = 0;
            String restaurantName = ""; //모든 가게 조회 위함
            String restaurantType = ""; //필터링 하지 않음

            List<RestaurantResDto> result = restaurantService.getRestaurants(lat, lon, range, order, page, restaurantName, restaurantType);

            //then
            assertThat(result).hasSize(1);
        }
    }

    @Nested
    @DisplayName("맛집의 상세정보를 가져온다.")
    class getRestaurant {

        @Test
        @DisplayName("성공")
        void success() {
            //given
            Member member = Member.builder()
                    .account("namse")
                    .password("password")
                    .lat(12.412)
                    .lon(235.12)
                    .build();
            memberRepository.save(member);

            Rating rating = Rating.builder()
                    .member(member)
                    .restaurant(restaurant)
                    .score(3)
                    .content("내용")
                    .createdAt(LocalDateTime.now())
                    .build();
            ratingRepository.save(rating);

            //when
            RestaurantDetailResDto result = restaurantService.getRestaurant(restaurant.getId());

            //then
            assertThat(result).isNotNull();
        }
    }
}

package com.wanted.teamV.controller;


import com.wanted.teamV.common.ControllerTest;
import com.wanted.teamV.dto.res.RestaurantDetailResDto;
import com.wanted.teamV.dto.res.RestaurantResDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RestaurantControllerTest extends ControllerTest {

    @Nested
    @DisplayName("맛집목록을 가져온다.")
    class getRestaurants {

        @Test
        @DisplayName("성공")
        void success() throws Exception {
            List<RestaurantResDto> response = List.of(
                    new RestaurantResDto("가게이름","도로명주소","우편번호",1.0,1.0)
            );
            given(restaurantService.getRestaurants(any(), any(), anyDouble(), any(), anyInt(), any(), any()))
                    .willReturn(response);

            mockMvc.perform(get("/restaurants")
                    .queryParam("lat", "12.000")
                    .queryParam("lon", "23.234")
                    .queryParam("range", "1.0")
                    .queryParam("order", "")
                    .queryParam("page", "0")
                    .queryParam("search", "")
                    .queryParam("filter", "")
            )
                    .andDo(print())
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("맛집 상세정보를 가져온다.")
    class getRestaurant {

        @Test
        @DisplayName("성공")
        void success() throws Exception {
            RestaurantDetailResDto response = RestaurantDetailResDto.builder()
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
                    .licenseDe(LocalDateTime.now())
                    .clsbizDe(LocalDateTime.now())
                    .locplcAr(1.0)
                    .gradFacltDivNm("")
                    .maleEnflpsnCnt(1)
                    .year(2005)
                    .multiUseBizestblYn(false)
                    .gradDivNm("")
                    .totFacltScale(1.9)
                    .femaleEnflpsnCnt(1)
                    .circumfrDivNm("")
                    .sanittnIndutypeNm("")
                    .totEmplyCnt(1)
                    .ratings(List.of())
                    .build();

            given(restaurantService.getRestaurant(anyLong()))
                    .willReturn(response);

            mockMvc.perform(get("/restaurants" + "/{id}", 1))
                    .andDo(print())
                    .andExpect(status().isOk());
        }
    }
}

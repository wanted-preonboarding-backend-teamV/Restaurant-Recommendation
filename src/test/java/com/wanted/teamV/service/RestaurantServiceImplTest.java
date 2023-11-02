package com.wanted.teamV.service;

import com.wanted.teamV.dto.res.RestaurantDistrictResDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RestaurantServiceImplTest {

    @Autowired
    RestaurantService restaurantService;

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
}

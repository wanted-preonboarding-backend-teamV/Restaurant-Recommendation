package com.wanted.teamV.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.wanted.teamV.dto.req.MemberJoinReqDto;
import com.wanted.teamV.dto.req.MemberLoginReqDto;
import com.wanted.teamV.dto.req.RatingCreateReqDto;
import com.wanted.teamV.entity.Restaurant;
import com.wanted.teamV.exception.ErrorCode;
import com.wanted.teamV.repository.RatingRepository;
import com.wanted.teamV.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
class RatingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestaurantRepository restaurantRepository;

    private String token;

    @BeforeEach
    void setUp() throws Exception {
        // 회원가입
        MemberJoinReqDto joinReqDto = new MemberJoinReqDto("mockUser", "mockUser1234!");
        MvcResult result = mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(joinReqDto))
                )
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();

        // 로그인
        MemberLoginReqDto loginReqDto = new MemberLoginReqDto("mockUser", "mockUser1234!");
        result = mockMvc.perform(post("/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReqDto))
                )
                .andExpect(status().isOk())
                .andReturn();

        response = result.getResponse().getContentAsString();
        token = JsonPath.parse(response).read("$.accessToken");

        // 맛집 생성
        Restaurant mockRestaurant = Restaurant.builder()
                .name("맛집1")
                .sigun("서울시")
                .type("패스트푸드")
                .roadnameAddress("서울시 송파구")
                .lotAddress("서울시 송파구")
                .zipCode("12345")
                .lat(38.1234)
                .lon(128.123)
                .averageRating(0.0)
                .build();

        restaurantRepository.save(mockRestaurant);
    }

    @Test
    @DisplayName("평가 생성 - 성공")
    void createRating() throws Exception {
        //given
        long restaurantId = 1L;
        int score = 5;
        String content = "맛있어요!";

        RatingCreateReqDto request = RatingCreateReqDto.builder()
                .restaurantId(restaurantId)
                .score(score)
                .content(content)
                .build();

        //when & then
        mockMvc.perform(post("/ratings")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();
    }

    @Nested
    @DisplayName("평가 생성 - 실패")
    class createRatingFailure {
        @Test
        @DisplayName("음식점이 존재하지 않을 때")
        void createRating_RestaurantNotFound() throws Exception {
            //given
            long restaurantId = 100L;
            int score = 5;
            String content = "맛있어요!";

            RatingCreateReqDto request = RatingCreateReqDto.builder()
                    .restaurantId(restaurantId)
                    .score(score)
                    .content(content)
                    .build();

            //when & then
            mockMvc.perform(post("/ratings")
                            .header("Authorization", "Bearer " + token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.errorCode").value(ErrorCode.RESTAURANT_NOT_FOUND.name()))
                    .andExpect(jsonPath("$.message").value(ErrorCode.RESTAURANT_NOT_FOUND.getMessage()))
                    .andDo(print());
        }
    }
}
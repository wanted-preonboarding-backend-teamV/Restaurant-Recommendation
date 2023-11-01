package com.wanted.teamV.service.impl;

import com.wanted.teamV.dto.req.RatingCreateReqDto;
import com.wanted.teamV.entity.Member;
import com.wanted.teamV.entity.Rating;
import com.wanted.teamV.entity.Restaurant;
import com.wanted.teamV.exception.CustomException;
import com.wanted.teamV.exception.ErrorCode;
import com.wanted.teamV.repository.MemberRepository;
import com.wanted.teamV.repository.RatingRepository;
import com.wanted.teamV.repository.RestaurantRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RatingServiceImplTest {

    @InjectMocks
    private RatingServiceImpl ratingService;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private MemberRepository memberRepository;

    @Test
    @DisplayName("평가 생성 - 성공")
    void createRating_Success() {
        //given
        long restaurantId = 1L;

        RatingCreateReqDto requestDto = RatingCreateReqDto.builder()
                .restaurantId(1L)
                .score(5)
                .content("맛있어요!")
                .build();

        Member mockMember = mock(Member.class);
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(mockMember));

        Restaurant mockRestaurant = mock(Restaurant.class);
        when(restaurantRepository.findById(anyLong())).thenReturn(Optional.of(mockRestaurant));

        when(mockRestaurant.getId()).thenReturn(restaurantId);
        when(ratingRepository.calculateAverageRatingByRestaurant(mockRestaurant.getId())).thenReturn((double) requestDto.getScore());

        //when
        ratingService.createRating(requestDto);

        //then
        verify(ratingRepository, times(1)).save(any(Rating.class));
        verify(restaurantRepository, times(1)).updateAverageRating(restaurantId, requestDto.getScore());
        assertEquals(5.0, ratingRepository.calculateAverageRatingByRestaurant(mockRestaurant.getId()));
    }

    @Test
    @DisplayName("평가 생성 - 실패")
    void createRating_Failure() {
        //given
        long restaurantId = 2L;

        RatingCreateReqDto requestDto = RatingCreateReqDto.builder()
                .restaurantId(1L)
                .score(5)
                .content("맛있어요!")
                .build();

        Member mockMember = mock(Member.class);

        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(mockMember));
        when(restaurantRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when & then
        CustomException exception = assertThrows(CustomException.class, () -> ratingService.createRating(requestDto));
        assertEquals(ErrorCode.RESTAURANT_NOT_FOUND, exception.getErrorCode());
    }

}
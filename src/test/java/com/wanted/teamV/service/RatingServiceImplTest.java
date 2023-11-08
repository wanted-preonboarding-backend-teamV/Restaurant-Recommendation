package com.wanted.teamV.service;

import com.wanted.teamV.dto.req.RatingCreateReqDto;
import com.wanted.teamV.entity.Member;
import com.wanted.teamV.entity.Rating;
import com.wanted.teamV.entity.Restaurant;
import com.wanted.teamV.exception.CustomException;
import com.wanted.teamV.exception.ErrorCode;
import com.wanted.teamV.repository.MemberRepository;
import com.wanted.teamV.repository.RatingRepository;
import com.wanted.teamV.repository.RestaurantRepository;
import com.wanted.teamV.service.averageRating.ScoreCalculationQueue;
import com.wanted.teamV.service.impl.RatingServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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

    @Mock
    private ScoreCalculationQueue scoreCalculationQueue;

    @Test
    @DisplayName("평가 생성 - 성공")
    void createRating_Success() {
        //given
        long memberId = 1L;

        RatingCreateReqDto requestDto = RatingCreateReqDto.builder()
                .restaurantId(1L)
                .score(5)
                .content("맛있어요!")
                .build();

        Member mockMember = mock(Member.class);
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(mockMember));

        Restaurant mockRestaurant = mock(Restaurant.class);
        when(restaurantRepository.findById(anyLong())).thenReturn(Optional.of(mockRestaurant));

        //when
        ratingService.createRating(memberId, requestDto);

        //then
        verify(ratingRepository, times(1)).save(any(Rating.class));
        verify(scoreCalculationQueue, times(1)).enqueueRatingForCalculation(any(Rating.class));
    }

    @Nested
    @DisplayName("평가 생성 - 실패")
    class createRatingFailure {
        @Test
        @DisplayName("음식점이 존재하지 않을 때")
        void createRating_RestaurantNotFound() {
            //given
            RatingCreateReqDto requestDto = RatingCreateReqDto.builder()
                    .restaurantId(1L)
                    .score(5)
                    .content("맛있어요!")
                    .build();

            Member mockMember = mock(Member.class);

            when(memberRepository.findById(anyLong())).thenReturn(Optional.of(mockMember));
            when(restaurantRepository.findById(anyLong())).thenReturn(Optional.empty());

            //when & then
            CustomException exception = assertThrows(CustomException.class, () -> ratingService.createRating(anyLong(), requestDto));
            assertEquals(ErrorCode.RESTAURANT_NOT_FOUND, exception.getErrorCode());
        }

        @Test
        @DisplayName("Member가 존재하지 않을 때")
        void createRating_MemberNotFound() {
            // given
            RatingCreateReqDto requestDto = RatingCreateReqDto.builder()
                    .restaurantId(1L)
                    .score(5)
                    .content("맛있어요!")
                    .build();

            when(memberRepository.findById(anyLong())).thenReturn(Optional.empty());

            // when & then
            CustomException exception = assertThrows(CustomException.class, () -> ratingService.createRating(1L, requestDto));
            assertEquals(ErrorCode.INVALID_REQUEST, exception.getErrorCode());
        }
    }

}
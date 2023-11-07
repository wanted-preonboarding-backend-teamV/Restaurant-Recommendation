package com.wanted.teamV.service;

import com.wanted.teamV.dto.req.MemberJoinReqDto;
import com.wanted.teamV.dto.req.MemberLoginReqDto;
import com.wanted.teamV.dto.res.MemberInfoResDto;
import com.wanted.teamV.dto.res.MemberTokenResDto;
import com.wanted.teamV.entity.Member;
import com.wanted.teamV.exception.CustomException;
import com.wanted.teamV.exception.ErrorCode;
import com.wanted.teamV.repository.MemberRepository;
import com.wanted.teamV.service.impl.AuthTokenCreator;
import com.wanted.teamV.service.impl.MemberServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    @InjectMocks
    private MemberServiceImpl memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthTokenCreator authTokenCreator;

    @Test
    @DisplayName("회원가입 - 성공")
    @Transactional
    public void join() throws Exception {
        //given
        MemberJoinReqDto request = new MemberJoinReqDto("test1234", "test1234!@#$");
        when(memberRepository.existsByAccount(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encryptedPassword");

        //when
        ResponseEntity<Void> response = memberService.join(request);

        //then
        verify(memberRepository, times(1)).save(any(Member.class));
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("회원가입 - 중복 계정")
    @Transactional
    public void join_duplicate_account() throws Exception {
        //given
        MemberJoinReqDto request = new MemberJoinReqDto("test1234", "test1234!@#$");
        when(memberRepository.existsByAccount(anyString())).thenReturn(true);

        //when & then
        CustomException customException = assertThrows(CustomException.class, () -> memberService.join(request));

        assertEquals(ErrorCode.DUPLICATE_ACCOUNT, customException.getErrorCode());
    }

    @Test
    @DisplayName("로그인 - 성공")
    @Transactional
    public void login() throws Exception {
        //given
        Member member = new Member("test1234", "hashedPassword", 36.123456, 127.654321, true);

        when(memberRepository.getByAccount("test1234")).thenReturn(member);
        when(passwordEncoder.matches("test1234!@#$", "hashedPassword")).thenReturn(true);
        when(authTokenCreator.createAuthToken(member.getId())).thenReturn("qwerqrwqrr.12323131.assdas");

        //when
        MemberLoginReqDto request = new MemberLoginReqDto("test1234", "test1234!@#$");
        MemberTokenResDto response = memberService.login(request);

        //then
        assertNotNull(response.accessToken());

    }

    @Test
    @DisplayName("로그인 - 실패(잘못된 비밀번호)")
    @Transactional
    public void login_invalid_password() throws Exception {
        //given
        Member member = new Member("test1234", "hashedPassword", 36.123456, 127.654321, true);

        when(memberRepository.getByAccount("test1234")).thenReturn(member);
        when(passwordEncoder.matches("wrongPassword", "hashedPassword")).thenReturn(false);

        //when
        MemberLoginReqDto request = new MemberLoginReqDto("test1234", "wrongPassword");

        //then
        CustomException exception = assertThrows(CustomException.class, () -> memberService.login(request));
        assertEquals(ErrorCode.INVALID_PASSWORD, exception.getErrorCode());
    }

    @Test
    @DisplayName("조회 - 성공")
    public void getMember() throws Exception {
        //given
        Member member = new Member("test1234", "hashedPassword", 36.123456, 127.654321, true);

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        //when
        MemberInfoResDto response = memberService.getMember(1L);

        //then
        assertEquals(member.getId(), response.getId());
        assertEquals(member.getAccount(), response.getAccount());
        assertEquals(member.getLat(), response.getLat());
        assertEquals(member.getLon(), response.getLon());
        assertEquals(member.getRecommend(), response.getRecommend());

    }

    @Test
    @DisplayName("설정 업데이트 - 성공")
    public void updateMember() throws Exception {
        //given
        Double updatedLat = 36.123456;
        Double updatedLon = 127.123345;
        Boolean updatedRecommend = true;

        Member member = Member.builder()
                .account("test1234")
                .password("test1234!@#$")
                .lat(null)
                .lon(null)
                .build();

        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));

        //when
        MemberInfoResDto updateResponse = memberService.updateMember(member.getId(), updatedLat, updatedLon, updatedRecommend);

        //then
        assertNotNull(updateResponse);
        assertEquals(member.getId(), updateResponse.getId());
        assertEquals(updatedLat, updateResponse.getLat());
        assertEquals(updatedLon, updateResponse.getLon());
        assertEquals(updatedRecommend, updateResponse.getRecommend());

        verify(memberRepository).updateMemberFields(eq(member.getId()), eq(updatedLat), eq(updatedLon), eq(updatedRecommend));
    }

    @Test
    @DisplayName("설정 업데이트 - 실패(위도가 비어있을 경우")
    public void updateMember_null_rat() throws Exception {
        //given
        Double updatedLat = null;
        Double updatedLon = 127.123345;
        Boolean updatedRecommend = true;

        Member member = Member.builder()
                .account("test1234")
                .password("test1234!@#$")
                .lat(null)
                .lon(null)
                .build();

        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));

        //when & then
        CustomException exception = assertThrows(CustomException.class, () -> memberService.updateMember(member.getId(), updatedLat, updatedLon, updatedRecommend));
        assertEquals(ErrorCode.NULL_LAT_VALUE, exception.getErrorCode());
    }

    @Test
    @DisplayName("설정 업데이트 - 실패(경도가 비어있을 경우")
    public void updateMember_null_lon() throws Exception {
        //given
        Double updatedLat = 36.123464;
        Double updatedLon = null;
        Boolean updatedRecommend = true;

        Member member = Member.builder()
                .account("test1234")
                .password("test1234!@#$")
                .lat(null)
                .lon(null)
                .build();

        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));

        //when & then
        CustomException exception = assertThrows(CustomException.class, () -> memberService.updateMember(member.getId(), updatedLat, updatedLon, updatedRecommend));
        assertEquals(ErrorCode.NULL_LON_VALUE, exception.getErrorCode());
    }

    @Test
    @DisplayName("설정 업데이트 - 실패(위도 범위를 벗어난 경우")
    public void updateMember_invalid_rat_range() throws Exception {
        //given
        Double updatedLat = 20.0;
        Double updatedLon = 127.123345;
        Boolean updatedRecommend = true;

        Member member = Member.builder()
                .account("test1234")
                .password("test1234!@#$")
                .lat(null)
                .lon(null)
                .build();

        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));

        //when & then
        CustomException exception = assertThrows(CustomException.class, () -> memberService.updateMember(member.getId(), updatedLat, updatedLon, updatedRecommend));
        assertEquals(ErrorCode.INVALID_LAT_RANGE, exception.getErrorCode());
    }

    @Test
    @DisplayName("설정 업데이트 - 실패(경도 범위를 벗어난 경우")
    public void updateMember_invalid_lon_range() throws Exception {
        //given
        Double updatedLat = 36.123451;
        Double updatedLon = 1000.0;
        Boolean updatedRecommend = true;

        Member member = Member.builder()
                .account("test1234")
                .password("test1234!@#$")
                .lat(null)
                .lon(null)
                .build();

        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));

        //when & then
        CustomException exception = assertThrows(CustomException.class, () -> memberService.updateMember(member.getId(), updatedLat, updatedLon, updatedRecommend));
        assertEquals(ErrorCode.INVALID_LON_RANGE, exception.getErrorCode());
    }
}

package com.wanted.teamV.service;

import com.wanted.teamV.dto.req.MemberJoinReqDto;
import com.wanted.teamV.dto.req.MemberLoginReqDto;
import com.wanted.teamV.dto.res.MemberTokenResDto;
import com.wanted.teamV.entity.Member;
import com.wanted.teamV.exception.CustomException;
import com.wanted.teamV.exception.ErrorCode;
import com.wanted.teamV.repository.MemberRepository;
import com.wanted.teamV.service.impl.AuthTokenCreator;
import com.wanted.teamV.service.impl.MemberServiceImpl;
import org.junit.jupiter.api.BeforeEach;
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

import static org.assertj.core.api.Assertions.assertThat;
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
    public void login() throws Exception {
        //given
        Member member = Member.testMemberEntity();

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
        Member member = Member.testMemberEntity();

        when(memberRepository.getByAccount("test1234")).thenReturn(member);
        when(passwordEncoder.matches("wrongPassword", "hashedPassword")).thenReturn(false);

        //when
        MemberLoginReqDto request = new MemberLoginReqDto("test1234", "wrongPassword");

        //then
        CustomException exception = assertThrows(CustomException.class, () -> memberService.login(request));
        assertEquals(ErrorCode.INVALID_PASSWORD, exception.getErrorCode());
    }
}

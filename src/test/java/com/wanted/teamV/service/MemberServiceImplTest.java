package com.wanted.teamV.service;

import com.wanted.teamV.dto.req.MemberJoinReqDto;
import com.wanted.teamV.entity.Member;
import com.wanted.teamV.exception.CustomException;
import com.wanted.teamV.exception.ErrorCode;
import com.wanted.teamV.repository.MemberRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
}

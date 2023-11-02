package com.wanted.teamV.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanted.teamV.dto.LoginMember;
import com.wanted.teamV.dto.req.MemberJoinReqDto;
import com.wanted.teamV.dto.req.MemberLoginReqDto;
import com.wanted.teamV.dto.req.MemberUpdateReqDto;
import com.wanted.teamV.dto.res.MemberInfoResDto;
import com.wanted.teamV.dto.res.MemberTokenResDto;
import com.wanted.teamV.entity.Member;
import com.wanted.teamV.exception.CustomException;
import com.wanted.teamV.exception.ErrorCode;
import com.wanted.teamV.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@WebMvcTest({MemberController.class})
class MemberControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected MemberService memberService;

    @Test
    @DisplayName("회원가입 - 성공")
    public void join() throws Exception {
        //given
        MemberJoinReqDto request = new MemberJoinReqDto("test1234", "test1234!@");

        //when & then
        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 - 중복 계정")
    public void join_duplicate_account() throws Exception {
        //given
        MemberJoinReqDto request = new MemberJoinReqDto("test1234", "test1234!@@#$");

        doThrow(new CustomException(ErrorCode.DUPLICATE_ACCOUNT)).when(memberService).join(request);

        //when & then
        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 - 비밀번호 형식 오류")
    public void join_invalid_password() throws Exception {
        //given
        MemberJoinReqDto request = new MemberJoinReqDto("test1234", "test1234");

        //when & then
        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 - 성공")
    public void login() throws Exception {
        //given
        MemberLoginReqDto request = new MemberLoginReqDto("test1234", "test1234!@#$");
        MemberTokenResDto tokenResponse = new MemberTokenResDto("123456789qwerttyadsccvzzsadwdqadwa.asddwdad");

        when(memberService.login(request)).thenReturn(tokenResponse);

        //when & then
        mockMvc.perform(post("/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    @DisplayName("로그인 - 실패(잘못된 비밀번호)")
    public void login_invalid_password() throws Exception {
        //given
        MemberLoginReqDto request = new MemberLoginReqDto("test1234", "wrongPassword");

        when(memberService.login(request)).thenThrow(new CustomException(ErrorCode.INVALID_PASSWORD));

        //when & then
        mockMvc.perform(post("/members/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andDo(print());

    }

    @Test
    @DisplayName("조회 - 성공")
    public void getMember() throws Exception {
        //given
        MemberLoginReqDto request = new MemberLoginReqDto("test1234", "test1234!@#$");
        MemberTokenResDto tokenResponse = new MemberTokenResDto("123456789qwerttyadsccvzzsadwdqadwa.asddwdad");

        MemberInfoResDto response = MemberInfoResDto.builder()
                .id(1L)
                .account("test1234")
                .lat(null)
                .lon(null)
                .recommend(true)
                .build();

        when(memberService.login(request)).thenReturn(tokenResponse);
        when(memberService.getMember(anyLong())).thenReturn(response);

        //when & then
        mockMvc.perform(get("/members")
                        .header("Authorization", "Bearer " + tokenResponse.accessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.getId()))
                .andExpect(jsonPath("$.account").value(response.getAccount()))
                .andExpect(jsonPath("$.lat").value(response.getLat()))
                .andExpect(jsonPath("$.lon").value(response.getLon()))
                .andExpect(jsonPath("$.recommend").value(response.getRecommend()))
                .andDo(print());
    }

    @Test
    @DisplayName("설정 업데이트 - 성공")
    public void updateMember() throws Exception {
        //given
        MemberUpdateReqDto updateRequest = new MemberUpdateReqDto(37.123456, 127.123456, true);
        MemberInfoResDto updateMember = MemberInfoResDto.builder()
                .id(1L)
                .account("test1234")
                .lat(updateRequest.lat())
                .lon(updateRequest.lon())
                .recommend(updateRequest.recommend())
                .build();

        when(memberService.updateMember(anyLong(), anyDouble(), anyDouble(), anyBoolean())).thenReturn(updateMember);

        //when & then
        mockMvc.perform(patch("/members")
                        .header("Authorization", "Bearer tokens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updateMember.getId()))
                .andExpect(jsonPath("$.account").value(updateMember.getAccount()))
                .andExpect(jsonPath("$.lat").value(updateMember.getLat()))
                .andExpect(jsonPath("$.lon").value(updateMember.getLon()))
                .andExpect(jsonPath("$.recommend").value(updateMember.getRecommend()))
                .andDo(print());
    }

    @Test
    @DisplayName("설정 업데이트 - 실패(위도가 null인 경우)")
    public void updateMember_null_lat() throws Exception {
        //given
        MemberUpdateReqDto updateRequest = new MemberUpdateReqDto(null, 127.123456, true);

        when(memberService.updateMember(anyLong(), eq(null), anyDouble(), anyBoolean()))
                .thenThrow(new CustomException(ErrorCode.NULL_LAT_VALUE));

        //when & then
        mockMvc.perform(patch("/members")
                        .header("Authorization", "Bearer tokens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("설정 업데이트 - 실패(경도가 null인 경우)")
    public void updateMember_null_lon() throws Exception {
        //given
        MemberUpdateReqDto updateRequest = new MemberUpdateReqDto(36.123456, null, true);

        when(memberService.updateMember(anyLong(), anyDouble(), eq(null), anyBoolean()))
                .thenThrow(new CustomException(ErrorCode.NULL_LON_VALUE));

        //when & then
        mockMvc.perform(patch("/members")
                        .header("Authorization", "Bearer tokens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("설정 업데이트 - 실패(위도 범위를 벗어난 경우)")
    public void updateMember_invalid_range_lat() throws Exception {
        //given
        MemberUpdateReqDto updateRequest = new MemberUpdateReqDto(26.123456, 125.123456, true);

        when(memberService.updateMember(anyLong(), anyDouble(), anyDouble(), anyBoolean()))
                .thenThrow(new CustomException(ErrorCode.INVALID_LAT_RANGE));

        //when & then
        mockMvc.perform(patch("/members")
                        .header("Authorization", "Bearer tokens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("설정 업데이트 - 실패(경도 범위를 벗어난 경우)")
    public void updateMember_invalid_range_lon() throws Exception {
        //given
        MemberUpdateReqDto updateRequest = new MemberUpdateReqDto(36.123456, 1000.0, true);

        when(memberService.updateMember(anyLong(), anyDouble(), anyDouble(), anyBoolean()))
                .thenThrow(new CustomException(ErrorCode.INVALID_LON_RANGE));

        //when & then
        mockMvc.perform(patch("/members")
                        .header("Authorization", "Bearer tokens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}

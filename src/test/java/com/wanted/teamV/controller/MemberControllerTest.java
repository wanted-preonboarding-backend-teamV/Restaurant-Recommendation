package com.wanted.teamV.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanted.teamV.common.ControllerTest;
import com.wanted.teamV.dto.req.MemberJoinReqDto;
import com.wanted.teamV.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemberControllerTest extends ControllerTest {

    @Test
    @DisplayName("회원가입 - 성공")
    public void join_Success() throws Exception {
        //given
        MemberJoinReqDto request = new MemberJoinReqDto("test1234", "test1234!@");

        //when & then
        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print());
    }
}

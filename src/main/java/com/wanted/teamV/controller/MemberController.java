package com.wanted.teamV.controller;

import com.wanted.teamV.dto.LoginMember;
import com.wanted.teamV.dto.req.MemberJoinReqDto;
import com.wanted.teamV.dto.req.MemberLoginReqDto;
import com.wanted.teamV.dto.req.MemberUpdateReqDto;
import com.wanted.teamV.dto.res.MemberInfoResDto;
import com.wanted.teamV.dto.res.MemberTokenResDto;
import com.wanted.teamV.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping()
    public ResponseEntity<String> join(
            @Valid @RequestBody MemberJoinReqDto memberJoinReqDto
    ) {
        memberService.join(memberJoinReqDto);
        return ResponseEntity.ok("OK");
    }

    @PostMapping("/login")
    public ResponseEntity<MemberTokenResDto> login(
            @RequestBody MemberLoginReqDto memberLoginReqDto
    ) {
        MemberTokenResDto memberTokenResDto = memberService.login(memberLoginReqDto);
        return ResponseEntity.ok(memberTokenResDto);
    }

    @GetMapping()
    public ResponseEntity<MemberInfoResDto> getMember(
            @AuthenticationPrincipal LoginMember loginMember
    ) {
        MemberInfoResDto response = memberService.getMember(loginMember.id());
        return ResponseEntity.ok(response);
    }

    @PatchMapping()
    public ResponseEntity<MemberInfoResDto> updateMember(
            @AuthenticationPrincipal LoginMember loginMember,
            @RequestBody MemberUpdateReqDto memberUpdateReqDto
    ) {
        Double lat = memberUpdateReqDto.lat();
        Double lon = memberUpdateReqDto.lon();
        Boolean recommend = memberUpdateReqDto.recommend();

        MemberInfoResDto response = memberService.updateMember(loginMember.id(), lat, lon, recommend);
        return ResponseEntity.ok(response);
    }
}

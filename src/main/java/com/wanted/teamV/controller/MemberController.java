package com.wanted.teamV.controller;

import com.wanted.teamV.dto.LoginMember;
import com.wanted.teamV.dto.req.MemberJoinReqDto;
import com.wanted.teamV.dto.req.MemberLoginReqDto;
import com.wanted.teamV.dto.req.MemberUpdateReqDto;
import com.wanted.teamV.dto.res.MemberInfoResDto;
import com.wanted.teamV.dto.res.MemberTokenResDto;
import com.wanted.teamV.entity.Member;
import com.wanted.teamV.exception.CustomException;
import com.wanted.teamV.exception.ErrorCode;
import com.wanted.teamV.repository.MemberRepository;
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
    private final MemberRepository memberRepository;

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
        Member member = memberRepository.findById(loginMember.id()).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        Double lat = memberUpdateReqDto.lat();
        Double lon = memberUpdateReqDto.lon();
        Boolean recommend = memberUpdateReqDto.recommend();

        if (memberUpdateReqDto.lat() == null) {
            lat = member.getLat();
        }

        if (memberUpdateReqDto.lon() == null) {
            lon = member.getLon();
        }

        if (memberUpdateReqDto.recommend() == null) {
            recommend = member.getRecommend();
        }

        MemberInfoResDto response = memberService.updateMember(loginMember.id(), lat, lon, recommend);
        return ResponseEntity.ok(response);
    }
}

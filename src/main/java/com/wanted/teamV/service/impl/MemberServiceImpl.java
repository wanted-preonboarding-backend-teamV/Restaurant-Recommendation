package com.wanted.teamV.service.impl;

import com.wanted.teamV.dto.req.MemberJoinReqDto;
import com.wanted.teamV.dto.req.MemberLoginReqDto;
import com.wanted.teamV.dto.res.MemberInfoResDto;
import com.wanted.teamV.dto.res.MemberTokenResDto;
import com.wanted.teamV.entity.Member;
import com.wanted.teamV.exception.CustomException;
import com.wanted.teamV.exception.ErrorCode;
import com.wanted.teamV.repository.MemberRepository;
import com.wanted.teamV.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthTokenCreator authTokenCreator;

    @Override
    public ResponseEntity<Void> join(MemberJoinReqDto memberJoinReqDto) {
        validateUniqueAccount(memberJoinReqDto.account());

        String encryptedPassword = passwordEncoder.encode(memberJoinReqDto.password());

        Member member = Member.builder()
                .account(memberJoinReqDto.account())
                .password(encryptedPassword)
                .lat(null)
                .lon(null)
                .build();
        memberRepository.save(member);

        return ResponseEntity.ok().build();
    }

    @Override
    public MemberTokenResDto login(MemberLoginReqDto memberLoginReqDto) {
        Member member = memberRepository.getByAccount(memberLoginReqDto.account());

        if (!passwordEncoder.matches(memberLoginReqDto.password(), member.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }

        String accessToken = authTokenCreator.createAuthToken(member.getId());
        return new MemberTokenResDto(accessToken);
    }

    @Override
    public Long extractUserId(String accessToken) {
        return authTokenCreator.extractPayload(accessToken);
    }

    @Override
    public MemberInfoResDto getMember(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        MemberInfoResDto response = MemberInfoResDto.builder()
                .id(member.getId())
                .account(member.getAccount())
                .lat(member.getLat())
                .lon(member.getLon())
                .recommend(member.getRecommend())
                .build();

        return response;
    }

    @Override
    public MemberInfoResDto updateMember(Long memberId, Double lat, Double lon, Boolean recommend) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if (lat == null) {
            if (member.getLat() != null) {
                lat = member.getLat();
            } else {
                throw new CustomException(ErrorCode.NULL_LAT_VALUE);
            }
        }

        if (lon == null) {
            if (member.getLon() != null) {
                lon = member.getLon();
            } else {
                throw new CustomException(ErrorCode.NULL_LON_VALUE);
            }
        }

        recommend = (recommend == null) ? member.getRecommend() : recommend;

        if (lat < 33 || lat > 43) throw new CustomException(ErrorCode.INVALID_LAT_RANGE);
        if (lon < 124 || lon > 132) throw new CustomException(ErrorCode.INVALID_LON_RANGE);

        memberRepository.updateMemberFields(memberId, lat, lon, recommend);

        MemberInfoResDto response = MemberInfoResDto.builder()
                .id(member.getId())
                .account(member.getAccount())
                .lat(lat)
                .lon(lon)
                .recommend(recommend)
                .build();

        return response;
    }

    private void validateUniqueAccount(String account) {
        if (memberRepository.existsByAccount(account)) {
            throw new CustomException(ErrorCode.DUPLICATE_ACCOUNT);
        }
    }
}

package com.wanted.teamV.repository;

import com.wanted.teamV.entity.Member;

import java.util.List;

public interface MemberQRepository {
    List<Member> getAllMembersAgreedRecommendation();
}

package com.wanted.teamV.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wanted.teamV.entity.Member;
import com.wanted.teamV.repository.MemberQRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.wanted.teamV.entity.QMember.member;

@RequiredArgsConstructor
public class MemberQRepositoryImpl implements MemberQRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Member> getAllMembersAgreedRecommendation() {
        return queryFactory.selectFrom(member)
                .where(member.recommend.eq(true))
                .fetch();
    }

}
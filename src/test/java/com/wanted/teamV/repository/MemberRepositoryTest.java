package com.wanted.teamV.repository;

import com.wanted.teamV.config.QuerydslConfig;
import com.wanted.teamV.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Import({QuerydslConfig.class})
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TestEntityManager entityManager;


    @Test
    @DisplayName("점심 추천에 동의한 모든 사용자 얻기")
    void find_all_by_recommend_true_test() {
        //given
        Member member1 = new Member("member1", "member1234!", 37.5113482, 126.7786922, true);
        Member member2 = new Member( "member2", "member1234!", 37.5113482, 126.7786922, false);
        Member member3 = new Member("member3", "member1234!", 37.5113482, 126.7786922, true);

        entityManager.persist(member1);
        entityManager.persist(member2);
        entityManager.persist(member3);

        //when
        List<Member> membersWithRecommend = memberRepository.findAllByRecommendTrue();

        //then
        assertEquals(2, membersWithRecommend.size());
        assertTrue(membersWithRecommend.contains(member1));
        assertFalse(membersWithRecommend.contains(member2));
        assertTrue(membersWithRecommend.contains(member3));
    }
}
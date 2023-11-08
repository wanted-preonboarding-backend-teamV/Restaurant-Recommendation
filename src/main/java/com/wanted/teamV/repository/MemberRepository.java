package com.wanted.teamV.repository;

import com.wanted.teamV.entity.Member;
import com.wanted.teamV.exception.CustomException;
import com.wanted.teamV.exception.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByAccount(String account);

    Optional<Member> findByAccount(String account);

    default Member getByAccount(String account) {
        return findByAccount(account).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
    }

    @Modifying
    @Query("UPDATE Member m SET m.lat = :lat, m.lon = :lon, m.recommend = :recommend WHERE m.id = :memberId")
    void updateMemberFields(@Param("memberId") Long memberId, @Param("lat") Double lat, @Param("lon") Double lon, @Param("recommend") Boolean recommend);

    List<Member> findAllByRecommendTrue();

}

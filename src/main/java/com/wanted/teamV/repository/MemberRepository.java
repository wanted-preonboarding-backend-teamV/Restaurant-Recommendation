package com.wanted.teamV.repository;

import com.wanted.teamV.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}

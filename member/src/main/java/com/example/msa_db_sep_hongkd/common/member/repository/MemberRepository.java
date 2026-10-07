package com.example.msa_db_sep_hongkd.common.member.repository;

import com.example.msa_db_sep_hongkd.common.member.domain.Member;
import com.example.msa_db_sep_hongkd.common.member.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findByEmail(String email);
    List<Member> findAllByRole(Role role);
}

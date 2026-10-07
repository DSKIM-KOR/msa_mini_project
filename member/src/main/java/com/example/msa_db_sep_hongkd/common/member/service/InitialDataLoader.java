package com.example.msa_db_sep_hongkd.common.member.service;


import com.example.msa_db_sep_hongkd.common.member.domain.Member;
import com.example.msa_db_sep_hongkd.common.member.domain.Role;
import com.example.msa_db_sep_hongkd.common.member.repository.MemberRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

//CommandLineRunner를 구현함으로써, 해당 Commponent가 스프링 Bean으로 등록되는 시점에서 Run()메소드를 자동적으로 실행하도록 한다.
//(즉, run 될때 가장 먼저 이 클래스를 만들어라
@Component
public class InitialDataLoader implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public InitialDataLoader(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if(memberRepository.findByEmail("admin@naver.com").isPresent()) return;

        Member member = Member.builder()
                .name("admin")
                .email("admin@naver.com")
                .password(passwordEncoder.encode("admin1234"))
                .role(Role.ADMIN)
                .build();
        memberRepository.save(member);
    }
}

package com.example.msa_db_sep_hongkd.common.member.service;

import com.example.msa_db_sep_hongkd.common.member.domain.Member;
import com.example.msa_db_sep_hongkd.common.member.domain.Role;
import com.example.msa_db_sep_hongkd.common.member.domain.UserStatus;
import com.example.msa_db_sep_hongkd.common.member.dto.*;
import com.example.msa_db_sep_hongkd.common.member.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //회원가입
    public Long save(MemberSaveReqDTO memberSaveReqDTO){
        //OPtional = 있는지 없는지 확인한다(optional.ispresent).
        Optional<Member> optionalMember = memberRepository.findByEmail(memberSaveReqDTO.getEmail());
        if(optionalMember.isPresent()){
            throw new IllegalArgumentException(("기존에 존재하는 회원입니다"));
        }
        //비밀번호 암호화하기
        String password = passwordEncoder.encode(memberSaveReqDTO.getPassword());

        Member member =memberRepository.save(memberSaveReqDTO.toEntity(password));

        return member.getId();

    }
    public Member login(LoginDTO dto){
        boolean check = true;

        //email 존재 여부
        Optional<Member> optionalMember =memberRepository.findByEmail(dto.getEmail());
        if(!optionalMember.isPresent()){
            check=false;
        }
        //password 일치 여부(matches함수 중요. matches(dto.getPassword(),optional.get().getPassword()))
        if(!passwordEncoder.matches(dto.getPassword(),optionalMember.get().getPassword())){
            check=false;
        }
        if(!check){
            throw new IllegalArgumentException("email 또는 비밀번호가 일치하지 않습니다");
        }
        return optionalMember.get();
    }
    public MemberResponseDTO getMemberInfo(Long memberId){
        Member member = memberRepository.findById(memberId).orElseThrow(
                ()->new IllegalArgumentException("존재하지 않는 회원입니다"));
        return MemberResponseDTO.builder()
                .memberId(member.getId())
                .email(member.getEmail())
                .build();
    }
    @Transactional
    public void updateUserStatusActivate(Long memberId, String role){
        if(!Role.ADMIN.name().equals(role)){
            throw new IllegalArgumentException("관리자만 회원의 상태를 변경할 수 있습니다.");
        }
        Member member = memberRepository.findById(memberId).orElseThrow(
                ()->new IllegalArgumentException("존재하지 않는 회원입니다"));
        if(UserStatus.ACTIVATE.equals(member.getUserStatus())){
            throw new IllegalArgumentException("이미 활성화된 유저입니다");
        }
        member.updateStatus(UserStatus.ACTIVATE);
    }
    @Transactional
    public void updateUserStatusBanned(Long memberId, String role){
        if(!Role.ADMIN.name().equals(role)){
            throw new IllegalArgumentException("관리자만 회원의 상태를 변경할 수 있습니다.");
        }
        Member member = memberRepository.findById(memberId).orElseThrow(
                ()->new IllegalArgumentException("존재하지 않는 회원입니다"));
        if(UserStatus.BANNED.equals(member.getUserStatus())){
            throw new IllegalArgumentException("이미 비활성화된 유저입니다");
        }
        member.updateStatus(UserStatus.BANNED);
    }
    @Transactional
    public Long createSeller(SellerCreateDTO dto,String role){
        if(!Role.ADMIN.name().equals(role)){
            throw new IllegalArgumentException("관리자 계정만 판매자를 생성할 수 있습니다.");
        }
        Optional<Member> Optionalmember = memberRepository.findByEmail(dto.getEmail());
        if(Optionalmember.isPresent()){
            throw new IllegalArgumentException("이미 존재하는 판매자 계정입니다");
        }
        String password = passwordEncoder.encode(dto.getPassword());
        Member member = memberRepository.save(dto.toSellerEntity(password));
        return member.getId();
    }

    @Transactional
    public void deleteSeller(Long sellerId,String role) {
        if (!Role.ADMIN.name().equals(role)) {
            throw new IllegalArgumentException("계정 삭제는 관리자만 가능합니다");
        }
        Member seller = memberRepository.findById(sellerId).orElseThrow(() -> new IllegalArgumentException(
                "존재하지 않는 판매자 계정입니다."));
        if (!seller.getRole().equals(Role.SELLER)) {
            throw new IllegalArgumentException("해당 계정은 판매자 계정이 아닙니다.");
        }
        memberRepository.delete(seller);
    }
    @Transactional
    public List<MemberListDTO> getMemberList(String role){
        if(!Role.ADMIN.name().equals(role)){
            throw new IllegalArgumentException("해당 기능은 관리자만 가능합니다.");
        }
        return memberRepository.findAll().stream()
                .map(m-> MemberListDTO.builder()
                        .id(m.getId())
                        .name(m.getName())
                        .email(m.getEmail())
                        .role(m.getRole())
                        .userStatus(m.getUserStatus()).build())
                .collect(Collectors.toList());
    }
    @Transactional
    public List<MemberListDTO> getUserList(String role){
        if(!Role.ADMIN.name().equals(role)){
            throw new IllegalArgumentException("해당 기능은 관리자만 가능합니다.");
        }
        return memberRepository.findAllByRole(Role.USER).stream()
                .map(m-> MemberListDTO.builder()
                        .id(m.getId())
                        .name(m.getName())
                        .email(m.getEmail())
                        .role(m.getRole())
                        .userStatus(m.getUserStatus()).build())
                .collect(Collectors.toList());
    }
    @Transactional
    public List<MemberListDTO> getSellerList(String role){
        if(!Role.ADMIN.name().equals(role)){
            throw new IllegalArgumentException("해당 기능은 관리자만 가능합니다.");
        }
        return memberRepository.findAllByRole(Role.SELLER).stream()
                .map(m-> MemberListDTO.builder()
                        .id(m.getId())
                        .name(m.getName())
                        .email(m.getEmail())
                        .role(m.getRole())
                        .userStatus(m.getUserStatus()).build())
                .collect(Collectors.toList());
    }

}

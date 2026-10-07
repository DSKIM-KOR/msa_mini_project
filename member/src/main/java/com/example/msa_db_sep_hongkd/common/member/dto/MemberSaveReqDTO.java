package com.example.msa_db_sep_hongkd.common.member.dto;


import com.example.msa_db_sep_hongkd.common.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MemberSaveReqDTO {
    private String name;
    private String email;
    private String password;

    public Member toEntity(String encordedPassword){
        return Member.builder()
                .name(this.name)
                .email(this.email)
                .password(encordedPassword)
                .build();
    }
}

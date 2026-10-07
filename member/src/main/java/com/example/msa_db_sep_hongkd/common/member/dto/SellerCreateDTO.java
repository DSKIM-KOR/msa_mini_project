package com.example.msa_db_sep_hongkd.common.member.dto;

import com.example.msa_db_sep_hongkd.common.member.domain.Member;
import com.example.msa_db_sep_hongkd.common.member.domain.Role;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SellerCreateDTO {
    private String name;
    private String email;
    private String password;

    public Member toSellerEntity(String encordPassword){
        return Member.builder()
                .name(this.name)
                .email(this.email)
                .password(encordPassword)
                .role(Role.SELLER)
                .build();
    }
}

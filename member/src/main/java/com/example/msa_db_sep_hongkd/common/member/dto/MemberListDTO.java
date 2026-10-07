package com.example.msa_db_sep_hongkd.common.member.dto;

import com.example.msa_db_sep_hongkd.common.member.domain.Role;
import com.example.msa_db_sep_hongkd.common.member.domain.UserStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberListDTO {
    private Long id;
    private String name;
    private String email;
    private Role role;
    private UserStatus userStatus;
}

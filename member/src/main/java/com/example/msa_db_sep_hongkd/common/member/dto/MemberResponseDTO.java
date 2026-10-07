package com.example.msa_db_sep_hongkd.common.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberResponseDTO {
    private Long memberId;
    private String email;
}

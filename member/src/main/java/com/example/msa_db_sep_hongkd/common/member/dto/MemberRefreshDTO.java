package com.example.msa_db_sep_hongkd.common.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MemberRefreshDTO {
    private String refreshToken;

}

package com.example.msa_db_sep_hongkd.common.member.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginDTO {
    private String email;
    private String password;
}

package com.example.msa_db_sep_hongkd.common.member.domain;

import com.example.msa_db_sep_hongkd.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Role role =Role.USER;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private UserStatus userStatus = UserStatus.ACTIVATE;

    public void updateStatus(UserStatus userStatus){
        this.userStatus=userStatus;
    }

}

package com.example.msa_db_sep_hongkd.common.product.client;


import com.example.msa_db_sep_hongkd.common.product.dto.MemberDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "member-service")
public interface MemberServiceClient {
    @GetMapping("/member/{memberId}")
    MemberDTO getMember(@PathVariable Long memberId);
}

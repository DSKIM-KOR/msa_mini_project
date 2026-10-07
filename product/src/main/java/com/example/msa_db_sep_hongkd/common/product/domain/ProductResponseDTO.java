package com.example.msa_db_sep_hongkd.common.product.domain;


import lombok.Builder;
import lombok.Getter;

//상품 목록을 가져오기 위한 dto
@Getter
@Builder
public class ProductResponseDTO {
    private Long id;
    private String name;
    private Integer price;
    private Long stockQuantity;
    private String email;
}

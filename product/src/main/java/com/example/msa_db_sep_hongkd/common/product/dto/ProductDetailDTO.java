package com.example.msa_db_sep_hongkd.common.product.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductDetailDTO {
    private Long id;
    private String name;
    private Integer price;
    private Long stockQuantity;
}

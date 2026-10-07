package com.example.msa_db_sep_hongkd.common.product.domain;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProductAdminDTO {
    private Long id;
    private String name;
    private Integer price;
    private Long stockQuantity;
    private ProductStatus productStatus;
}

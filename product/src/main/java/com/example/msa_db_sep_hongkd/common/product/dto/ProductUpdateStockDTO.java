package com.example.msa_db_sep_hongkd.common.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateStockDTO {
    private Long id;
    private Long productQuantity;
}

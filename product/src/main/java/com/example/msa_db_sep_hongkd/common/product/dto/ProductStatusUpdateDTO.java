package com.example.msa_db_sep_hongkd.common.product.dto;

import com.example.msa_db_sep_hongkd.common.product.domain.ProductStatus;
import lombok.Data;

@Data
public class ProductStatusUpdateDTO {
    private ProductStatus productStatus;
}

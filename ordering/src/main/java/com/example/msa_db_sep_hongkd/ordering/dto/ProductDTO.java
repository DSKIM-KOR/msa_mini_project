package com.example.msa_db_sep_hongkd.ordering.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    private String name;
    private Integer price;
    private Integer stockQuantity;
}

package com.example.msa_db_sep_hongkd.ordering.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderCreateDTO {
    private Long productId;
    private Integer quantity;

}

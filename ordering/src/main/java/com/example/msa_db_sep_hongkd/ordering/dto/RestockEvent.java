package com.example.msa_db_sep_hongkd.ordering.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RestockEvent {
    private Long productId;
    private Integer quantity;
}

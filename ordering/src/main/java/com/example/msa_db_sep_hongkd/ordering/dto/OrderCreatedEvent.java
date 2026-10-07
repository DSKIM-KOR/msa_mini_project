package com.example.msa_db_sep_hongkd.ordering.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedEvent {
    private Long productId;
    private Integer quantity;
}

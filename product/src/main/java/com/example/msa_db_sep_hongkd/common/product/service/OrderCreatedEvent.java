package com.example.msa_db_sep_hongkd.common.product.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedEvent {
    private Long productId;
    private Long quantity;
}

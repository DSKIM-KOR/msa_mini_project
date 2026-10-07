package com.example.msa_db_sep_hongkd.ordering.dto;


import com.example.msa_db_sep_hongkd.ordering.domain.OrderStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderListDTO {
    private Long id;
    private Long productId;
    private String productName;
    private Integer orderPrice;
    private Integer quantity;
    private OrderStatus orderStatus;
}

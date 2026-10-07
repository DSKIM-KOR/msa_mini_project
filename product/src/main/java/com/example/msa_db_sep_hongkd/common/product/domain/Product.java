package com.example.msa_db_sep_hongkd.common.product.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer price;
    private Long stockQuantity;
    @Column(nullable = false)
    private Long memberId;
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ProductStatus productStatus = ProductStatus.WAITING;
    @Builder.Default
    private Long salesCount =0L;

    public void decreaseStock(int quantity){
        if(!this.productStatus.equals(ProductStatus.APPROVE)){
            throw new IllegalArgumentException("현재 판매 중인 상품이 아닙니다.");
        }
        if(this.stockQuantity<quantity){
            throw new IllegalArgumentException("재고가 부족합니다");
        }
        this.stockQuantity-=quantity;
    }
    public void increaseStock(Integer quantity){
        this.stockQuantity+=quantity;
    }
    public void updateProductStatus(ProductStatus productStatus){
        this.productStatus=productStatus;
    }
    public void increaseSalesCount(long quantity){
        this.salesCount +=quantity;
    }
    public void decreaseSalesCount(long quantity){
        this.salesCount=Math.max(0,this.salesCount - quantity); //0 이하로 내려가는 것을 막아줌
    }
}

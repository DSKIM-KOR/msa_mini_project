package com.example.msa_db_sep_hongkd.common.product.dto;


import com.example.msa_db_sep_hongkd.common.product.domain.Product;
import com.example.msa_db_sep_hongkd.common.product.domain.ProductStatus;
import com.example.msa_db_sep_hongkd.common.product.domain.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductCreateDTO {
    private String name;
    private Integer price;
    private Long stockQuantity;

    public Product toEntiy(Long memberId){
        return Product.builder()
                .name(this.name)
                .price(this.price)
                .stockQuantity(this.stockQuantity)
                .memberId(memberId)
                .build();
    }
}

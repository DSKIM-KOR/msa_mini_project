package com.example.msa_db_sep_hongkd.ordering.domain;


import com.example.msa_db_sep_hongkd.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "ordering")
public class Order extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Integer orderPrice;  // 주문 당시 단가 (또는 총액)

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private OrderStatus orderStatus =OrderStatus.ORDERED;

    public void cancel() {
        this.orderStatus = OrderStatus.CANCELED;
    }

}

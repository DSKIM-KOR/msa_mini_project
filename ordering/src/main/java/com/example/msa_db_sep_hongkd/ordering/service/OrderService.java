package com.example.msa_db_sep_hongkd.ordering.service;



import com.example.msa_db_sep_hongkd.ordering.client.ProductServiceClient;
import com.example.msa_db_sep_hongkd.ordering.domain.Order;
import com.example.msa_db_sep_hongkd.ordering.dto.*;
import com.example.msa_db_sep_hongkd.ordering.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductServiceClient productServiceClient;
    private final OrderKafkaProducer kafkaProducer;

    public OrderService(OrderRepository orderRepository, ProductServiceClient productServiceClient, OrderKafkaProducer kafkaProducer) {
        this.orderRepository = orderRepository;
        this.productServiceClient = productServiceClient;
        this.kafkaProducer = kafkaProducer;
    }
    @Transactional
    public Order orderCreate(OrderCreateDTO orderCreateDTO, Long memberId){
        ProductDTO product = productServiceClient.decreaseStock(
                orderCreateDTO.getProductId(),
                orderCreateDTO.getQuantity()
        );
        Order order = Order.builder()
                .memberId(memberId)
                .productId(orderCreateDTO.getProductId())
                .quantity(orderCreateDTO.getQuantity())
                .orderPrice(product.getPrice()*orderCreateDTO.getQuantity())
                .build();
        kafkaProducer.send("order-created-topic",new OrderCreatedEvent(order.getProductId(),order.getQuantity()));
        return orderRepository.save(order);
    }
    public List<OrderListDTO> orderList(Long memberId){
        List<Order> list= orderRepository.findByMemberIdOrderByCreatedTimeDesc(memberId);
        return list.stream()
                .map(o -> {
                    ProductDTO product = productServiceClient.getProduct(o.getProductId());
                    return OrderListDTO.builder()
                            .id(o.getId())
                            .productId(o.getProductId())
                            .productName(product.getName())
                            .orderPrice(o.getOrderPrice())
                            .quantity(o.getQuantity())
                            .orderStatus(o.getOrderStatus())
                            .build();
                }).collect(Collectors.toList());
    }

    public void canceledOrder(Long orderId,Long memberId){
        Order order=orderRepository.findById(orderId).orElseThrow(
                ()->new IllegalArgumentException("주문 정보를 확인해주세요"));
        if(!order.getMemberId().equals(memberId)){
            throw new IllegalArgumentException("본인의 주문만 취소할 수 있습니다");
        }
        order.cancel();
        kafkaProducer.send("restock-topic",new RestockEvent(order.getProductId(),order.getQuantity()));
        kafkaProducer.send("order-canceled-topic", new OrderCanceledEvent(order.getProductId(),order.getQuantity()));
    }
}

package com.example.msa_db_sep_hongkd.common.product.service;


import com.example.msa_db_sep_hongkd.common.product.domain.Product;
import com.example.msa_db_sep_hongkd.common.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductKafkaConsumer {
    private final ProductRepository productRepository;

    @KafkaListener(topics = "restock-topic", groupId = "product-group")
    @Transactional
    public void restock(RestockEvent event){
        Product product = productRepository.findById(event.getProductId()).orElseThrow(
                ()-> new IllegalArgumentException("존재하지 않는 상품입니다"));
        product.increaseStock(event.getQuantity());
    }
    @KafkaListener(topics="order-created-topic")
    @Transactional
    public void handlerOrderCreated(OrderCreatedEvent event){
        log.info("order-created 수신: {}", event.getProductId());
        Product product = productRepository.findById(event.getProductId()).orElseThrow(
                ()-> new IllegalArgumentException("존재하지 않는 상품입니다"));
        product.increaseSalesCount(event.getQuantity());
    }
    @KafkaListener(topics="order-canceled-topic")
    @Transactional
    public void handlerOrderCanceled(OrderCanceledEvent event){
        Product product = productRepository.findById(event.getProductId()).orElseThrow(
                ()-> new IllegalArgumentException("존재하지 않는 상품입니다"));
        product.decreaseSalesCount(event.getQuantity());
    }
}

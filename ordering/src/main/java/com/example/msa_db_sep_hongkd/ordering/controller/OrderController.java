package com.example.msa_db_sep_hongkd.ordering.controller;


import com.example.msa_db_sep_hongkd.ordering.domain.Order;
import com.example.msa_db_sep_hongkd.ordering.dto.OrderCreateDTO;
import com.example.msa_db_sep_hongkd.ordering.dto.OrderListDTO;
import com.example.msa_db_sep_hongkd.ordering.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
    @PostMapping("/create")
    public ResponseEntity<?> orderCreate(@RequestBody OrderCreateDTO orderCreateDTO, @RequestHeader("X-USER-ID") Long memberId){
        Order order= orderService.orderCreate(orderCreateDTO,memberId);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }
    @GetMapping("/orderList")
    public ResponseEntity<List<OrderListDTO>> orderList(@RequestHeader("X-USER-ID")Long memberId){
        List<OrderListDTO> list = orderService.orderList(memberId);
        return ResponseEntity.ok(list);
    }
    @PostMapping("/cancel/{orderId}")
    public ResponseEntity<?> canceledOrder(@PathVariable Long orderId,@RequestHeader("X-USER-ID")Long memberId){
        orderService.canceledOrder(orderId,memberId);
        return ResponseEntity.ok().build();
    }


}

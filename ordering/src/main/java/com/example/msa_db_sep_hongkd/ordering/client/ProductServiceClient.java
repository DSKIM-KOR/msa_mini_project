package com.example.msa_db_sep_hongkd.ordering.client;


import com.example.msa_db_sep_hongkd.ordering.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "product-service")
public interface ProductServiceClient {
    @PostMapping("/product/{productId}/decrease")
    ProductDTO decreaseStock(@PathVariable("productId") Long productId, @RequestParam("quantity") Integer quantity);
    @GetMapping("/product/{productId}")
    ProductDTO getProduct(@PathVariable("productId") Long productId);
}

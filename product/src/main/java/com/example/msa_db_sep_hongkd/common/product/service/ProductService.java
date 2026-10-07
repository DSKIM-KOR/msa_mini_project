package com.example.msa_db_sep_hongkd.common.product.service;


import com.example.msa_db_sep_hongkd.common.product.client.MemberServiceClient;
import com.example.msa_db_sep_hongkd.common.product.domain.*;
import com.example.msa_db_sep_hongkd.common.product.dto.MemberDTO;
import com.example.msa_db_sep_hongkd.common.product.dto.ProductCreateDTO;
import com.example.msa_db_sep_hongkd.common.product.dto.ProductDetailDTO;
import com.example.msa_db_sep_hongkd.common.product.dto.ProductStatusUpdateDTO;
import com.example.msa_db_sep_hongkd.common.product.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Member;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final MemberServiceClient memberServiceClient;


    public ProductService(ProductRepository productRepository, MemberServiceClient memberServiceClient) {
        this.productRepository = productRepository;
        this.memberServiceClient = memberServiceClient;
    }

    public Product productCreate(ProductCreateDTO dto, String memberId, String status, String role) {
        if (!UserStatus.ACTIVATE.name().equals(status)) {
            throw new IllegalArgumentException("정지된 회원은 상품을 등록할 수 없습니다.");
        }
        if (!Role.SELLER.name().equals(role)) {
            throw new IllegalArgumentException("판매자만 상품을 등록할 수 있습니다");
        }
        return productRepository.save(dto.toEntiy(Long.parseLong(memberId)));
    }

    @Transactional
    public void approveProduct(Long productId, String role) {
        if (!Role.ADMIN.name().equals(role)) {
            throw new IllegalArgumentException("관리자만 상품을 승인할 수 있습니다.");
        }
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new EntityNotFoundException("존재하지 않는 상품입니다"));
        if (product.getProductStatus().equals(ProductStatus.APPROVE)) {
            throw new IllegalArgumentException("이미 승인된 상품입니다");
        }
        product.updateProductStatus(ProductStatus.APPROVE);
    }
    @Transactional
    public void revokeProduct(Long productId, String role) {
        if (!Role.ADMIN.name().equals(role)) {
            throw new IllegalArgumentException("관리자만 상품을 거절할 수 있습니다.");
        }
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new EntityNotFoundException("존재하지 않는 상품입니다"));
        if (product.getProductStatus().equals(ProductStatus.REVOKE)) {
            throw new IllegalArgumentException("이미 거절된 상품입니다");
        }
        product.updateProductStatus(ProductStatus.REVOKE);
    }

    public List<ProductResponseDTO> productList() {
        List<Product> list = productRepository.findAllByProductStatus(ProductStatus.APPROVE);
        return list.stream().map(p -> {
            MemberDTO dto = memberServiceClient.getMember(p.getMemberId());
            return ProductResponseDTO.builder()
                    .id(p.getId())
                    .name(p.getName())
                    .price(p.getPrice())
                    .stockQuantity(p.getStockQuantity())
                    .email(dto.getEmail())
                    .build();
        }).collect(Collectors.toList());
    }

    public ProductDetailDTO productDetail(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 상품입니다"));
        ProductDetailDTO dto = ProductDetailDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .build();
        return dto;
    }

    @Transactional
    public ProductResponseDTO decreaseStock(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("없는 제품입니다"));
        product.decreaseStock(quantity);
        return ProductResponseDTO.builder()
                .id(product.getId())
                .price(product.getPrice())
                .build();
    }

    @Transactional(readOnly = true)
    public List<ProductAdminDTO> getProductManagementData(String role) {
        if (!Role.ADMIN.name().equals(role)) {
            throw new IllegalArgumentException("해당 페이지 접근은 관리자만 가능합니다");
        }
        return productRepository.findAll().stream()
                .map(p -> ProductAdminDTO.builder()
                        .id(p.getId())
                        .name(p.getName())
                        .price(p.getPrice())
                        .stockQuantity(p.getStockQuantity())
                        .productStatus(p.getProductStatus())
                        .build()).collect(Collectors.toList());
    }
    @Transactional
    public List<ProductResponseDTO> getTop5OrderBySalesCount(){
        return productRepository.findTop5ByOrderBySalesCount().stream().map(
                p -> ProductResponseDTO.builder()
                        .id(p.getId())
                        .name(p.getName())
                        .price(p.getPrice())
                        .stockQuantity(p.getStockQuantity())
                        .build()).collect(Collectors.toList());

    }
}

package com.example.msa_db_sep_hongkd.common.product.repository;


import com.example.msa_db_sep_hongkd.common.product.domain.Product;
import com.example.msa_db_sep_hongkd.common.product.domain.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findAllByProductStatus(ProductStatus productStatus);
    List<Product> findTop5ByOrderBySalesCount();
}

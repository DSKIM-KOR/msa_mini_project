package com.example.msa_db_sep_hongkd.common.product.controller;



import com.example.msa_db_sep_hongkd.common.product.domain.Product;
import com.example.msa_db_sep_hongkd.common.product.domain.ProductAdminDTO;
import com.example.msa_db_sep_hongkd.common.product.domain.ProductResponseDTO;
import com.example.msa_db_sep_hongkd.common.product.dto.ProductCreateDTO;
import com.example.msa_db_sep_hongkd.common.product.dto.ProductDetailDTO;
import com.example.msa_db_sep_hongkd.common.product.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createProduct(@RequestBody ProductCreateDTO dto,
                                           @RequestHeader("X-USER-ID")String memberId,
                                           @RequestHeader("X-USER-STATUS")String status,
                                           @RequestHeader("X-USER-ROLE")String role){
        System.out.println("ProductController : CreateProduct");
        Product product =productService.productCreate(dto,memberId,status,role);
        return new ResponseEntity<>(product.getId(), HttpStatus.CREATED);
    }
    @GetMapping("/list")
    public ResponseEntity<List<ProductResponseDTO>> listAll(){
        List<ProductResponseDTO> list = productService.productList();
        return ResponseEntity.ok(list);
    }
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDetailDTO> productDetail(@PathVariable Long productId) {
        ProductDetailDTO dto = productService.productDetail(productId);
        return ResponseEntity.ok(dto);
    }
    @PostMapping("/{productId}/decrease")
    public ResponseEntity<ProductResponseDTO> decreaseStock(
            @PathVariable("productId") Long productId,@RequestParam("quantity") Integer quantity){
        return ResponseEntity.ok(productService.decreaseStock(productId,quantity));
    }
    @PatchMapping("/{productId}/approve")
    public ResponseEntity<?> approveProduct(@PathVariable Long productId,@RequestHeader("X-USER-ROLE")String role){
        productService.approveProduct(productId,role);
        return new ResponseEntity<>(productId,HttpStatus.OK);
    }
    @PatchMapping("/{productId}/revoke")
    public ResponseEntity<?> revokeProduct(@PathVariable Long productId,@RequestHeader("X-USER-ROLE")String role){
        productService.revokeProduct(productId,role);
        return new ResponseEntity<>(productId,HttpStatus.OK);
    }
    @GetMapping("/admin/product/list")
    public ResponseEntity<List<ProductAdminDTO>> getProductManagementData(@RequestHeader("X-USER-ROLE")String role){
        List<ProductAdminDTO> list =productService.getProductManagementData(role);
        return ResponseEntity.ok(list);
    }
    @GetMapping("/bestSeller")
    public ResponseEntity<List<ProductResponseDTO>> getTop5OrderBySalesCount(){
        List<ProductResponseDTO> list = productService.getTop5OrderBySalesCount();
        return ResponseEntity.ok(list);
    }
}

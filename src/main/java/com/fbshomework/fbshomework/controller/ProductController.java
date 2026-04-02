package com.fbshomework.fbshomework.controller;

import com.fbshomework.fbshomework.entity.Product;
import com.fbshomework.fbshomework.service.ProductCacheService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductCacheService productCacheService;

    public ProductController(ProductCacheService productCacheService) {
        this.productCacheService = productCacheService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable Long id) {
        Product product = productCacheService.getProductById(id);
        if (product == null) {
            return ResponseEntity.status(404).body(Map.of("msg", "商品不存在"));
        }
        return ResponseEntity.ok(product);
    }
}
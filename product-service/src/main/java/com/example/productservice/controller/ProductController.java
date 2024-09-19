package com.example.productservice.controller;

import com.example.productservice.domain.Product;
import com.example.productservice.payload.ProductPurchaseRequest;
import com.example.productservice.payload.ProductPurchaseResponse;
import com.example.productservice.payload.ProductRequest;
import com.example.productservice.payload.ProductResponse;
import com.example.productservice.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findById(
            @PathVariable("id") Long id
    ){
        return ResponseEntity.ok(productService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> findAll(){
        return ResponseEntity.ok(productService.findAll());
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(
            @RequestBody @Valid ProductRequest request
    ){
        return ResponseEntity.ok(productService.createProduct(request));
    }

    @PostMapping("/purchase")
    public ResponseEntity<List<ProductPurchaseResponse>> purchaseProducts(
            @RequestBody @Valid List<ProductPurchaseRequest> requestList
    ){
        return ResponseEntity.ok(productService.purchaseProduct(requestList));
    }
}

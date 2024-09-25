package com.example.productservice.service;

import com.example.productservice.domain.Product;
import com.example.productservice.exception.ProductPurchaseRequestException;
import com.example.productservice.mapper.ProductMapper;
import com.example.productservice.payload.ProductPurchaseRequest;
import com.example.productservice.payload.ProductPurchaseResponse;
import com.example.productservice.payload.ProductRequest;
import com.example.productservice.payload.ProductResponse;
import com.example.productservice.repo.ProductRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductService {

    private final ProductMapper mapper;
    private final ProductRepo productRepo;
    private final KafkaTemplate<String, ProductPurchaseResponse> kafkaTemplate;

    public ProductService(ProductMapper mapper, ProductRepo productRepo, KafkaTemplate<String, ProductPurchaseResponse> kafkaTemplate) {
        this.mapper = mapper;
        this.productRepo = productRepo;
        this.kafkaTemplate = kafkaTemplate;
    }

    public ProductResponse findById(Long productId) {
        return productRepo.findById(productId)
                .map(mapper::toProductResponse)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Product not found with ID:: " + productId
                ));
    }

    public List<ProductResponse> findAll() {
        return productRepo.findAll()
                .stream()
                .map(mapper::toProductResponse)
                .toList();
    }

    public Product createProduct(ProductRequest request) {
        var product = mapper.toProduct(request);
        return productRepo.save(product);
    }


    public ProductPurchaseResponse addProductToBasket(ProductPurchaseRequest request) {
        Optional<Product> product = productRepo.findById(request.productId());
        var inStock = product.get();
        if (inStock.getQuantity()<= request.quantity()) {

            inStock.decreaseQuantity(request.quantity());
            productRepo.save(inStock);
        }else {
            throw new ProductPurchaseRequestException(
                    "Insufficient stock quantity for product ID:: "
                            + request.productId());
        }
        return mapper.toProductPurchaseResponse(inStock, request.quantity());
    }
    @KafkaListener(topics = "product-topic", groupId = "group0")
    public void listenShopTopic(ProductPurchaseRequest request) {
        log.info("basket received on topic: {}", request.productId());


        Product product = productRepo.findById(request.productId()).orElseThrow();
        if (!isValidShop(request, product)) {
            ProductPurchaseResponse response = ProductPurchaseResponse.builder().build();
            shopError(response);
        } else {
            ProductPurchaseResponse response = ProductPurchaseResponse.builder().build();
            shopSuccess(response);
        }

    }

    private boolean isValidShop(ProductPurchaseRequest request, Product product) {
        return product != null && product.getQuantity() >= request.quantity();
    }

    private void shopError(ProductPurchaseResponse response) {
        log.info("Add basket processing error {}.", response.getProductId());
        kafkaTemplate.send("product-basket-topic", response);
    }

    private void shopSuccess(ProductPurchaseResponse response) {
        log.info("Add basket successfully completed {}.", response.getProductId());
        kafkaTemplate.send("product-basket-topic", response);
    }

}

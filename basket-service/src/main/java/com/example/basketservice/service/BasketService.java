package com.example.basketservice.service;


import com.example.basketservice.domain.ProductDetails;
import com.example.basketservice.payload.ProductPurchaseRequest;
import com.example.basketservice.payload.ProductPurchaseResponse;
import com.example.basketservice.repository.ProductDetailsRepo;
import com.example.basketservice.security.SecurityCheck;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class BasketService {

    private final KafkaTemplate<String, ProductPurchaseRequest> kafkaTemplate;
    private final ProductDetailsRepo detailsRepo;


    public BasketService(KafkaTemplate<String, ProductPurchaseRequest> kafkaTemplate, ProductDetailsRepo detailsRepo) {
        this.kafkaTemplate = kafkaTemplate;
        this.detailsRepo = detailsRepo;
    }

    public List<ProductDetails> findByUserOwner() {
        return detailsRepo.findByUser_id(SecurityCheck.getUserIdFromSecurityContext());
    }

    public void addProductToBasket(ProductPurchaseRequest purchaseRequest) {

        kafkaTemplate.send("product-topic",purchaseRequest);
        log.info("addProduct {}.",purchaseRequest);

    }

    @KafkaListener(topics = "product-basket-topic", groupId = "group1")
    public void listenShopEvents(ProductPurchaseResponse response) {
        try {
            BigDecimal detailPrice = response.getPrice().multiply(BigDecimal.valueOf(response.getQuantity()));
            BigDecimal sale_Price = response.getPrice().multiply(BigDecimal.valueOf(response.getQuantity()));
            detailsRepo.save(ProductDetails.builder()
                    .product_id(response.getProductId())
                    .user_id(SecurityCheck.getUserIdFromSecurityContext())
                    .name(response.getName())
                    .quantity(response.getQuantity())
                    .price(detailPrice)
                    .sale_price(sale_Price).build());
        } catch (Exception e) {
            log.error("Basket processing error {}",response.getProductId());
        }
    }
}

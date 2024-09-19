package com.example.productservice.payload;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class ProductPurchaseResponse{
   private Long productId;
   private String name;
   private String description;
   private BigDecimal price;
   private BigDecimal sale_price;
   private Integer article;
   private Integer quantity;
}


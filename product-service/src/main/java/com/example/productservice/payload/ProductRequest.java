package com.example.productservice.payload;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProductRequest(Long id,
                             @NotNull(message = "Product name is required")
                             String name,
                             @NotNull(message = "Product description is required ")
                             String description,
                             @NotNull(message = "Product description is required ")
                             Integer article,
                             @Positive(message = "Price should be positive")
                             BigDecimal price,
                             @Positive(message = "Price should be positive")
                             BigDecimal sale_price,
                             @Positive(message = "Available quantity should be positive")
                             Integer quantity,
                             @NotNull(message = "Product category is required")
                             Long category_id) {

}

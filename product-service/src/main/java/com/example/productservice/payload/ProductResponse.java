package com.example.productservice.payload;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private Integer article;
    private BigDecimal price;
    private BigDecimal sale_price;
    private Integer quantity;
    private Long category_id;
    private String category_name;
    private String category_description;
}

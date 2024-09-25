package com.example.basketservice.payload;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Builder
@Data
public class ProductPurchaseRequest {
    @NotNull(message = "Product is required")
    private Long productId;
    @NotNull(message = "Quantity is required")
    private Integer quantity;
}

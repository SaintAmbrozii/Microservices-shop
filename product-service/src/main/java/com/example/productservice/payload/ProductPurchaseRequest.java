package com.example.productservice.payload;

import jakarta.validation.constraints.NotNull;

public record ProductPurchaseRequest(@NotNull(message = "Product is required")
                                     Long productId,
                                     @NotNull(message = "Quantity is required")
                                     Integer quantity) {
}

package com.example.productservice.mapper;

import com.example.productservice.domain.Category;
import com.example.productservice.domain.Product;
import com.example.productservice.payload.ProductPurchaseResponse;
import com.example.productservice.payload.ProductRequest;
import com.example.productservice.payload.ProductResponse;
import org.springframework.stereotype.Service;

@Service
public class ProductMapper {
    public Product toProduct(ProductRequest request) {
        return Product.builder()
                .id(request.id())
                .name(request.name())
                .description(request.description())
                .quantity(request.quantity())
                .price(request.price())
                .category(
                        Category.builder()
                                .id(request.category_id())
                                .build()
                )
                .build();
    }

    public ProductResponse toProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .price(product.getPrice())
                .sale_price(product.getSale_price())
                .name(product.getName())
                .description(product.getDescription())
                .quantity(product.getQuantity())
                .article(product.getActicle())
                .category_id(product.getCategory().getId())
                .category_name(product.getCategory().getName())
                .category_description(product.getCategory().getDescription())
                .build();
    }
    public ProductPurchaseResponse toProductPurchaseResponse(Product product, Integer quantity) {
        return ProductPurchaseResponse.builder()
                .productId(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(quantity)
                .build();
    }
}

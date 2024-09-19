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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
public class ProductService {

    private final ProductMapper mapper;
    private final ProductRepo productRepo;

    public ProductService(ProductMapper mapper, ProductRepo productRepo) {
        this.mapper = mapper;
        this.productRepo = productRepo;
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

    public List<ProductPurchaseResponse> purchaseProduct(
            List<ProductPurchaseRequest> requestList
    ) {
        var productIds = requestList
                .stream()
                .map(ProductPurchaseRequest::productId)
                .toList();


        List<Product> storedProducts = productRepo.findByListOfIds(productIds);

        if(productIds.size() != storedProducts.size()){
            throw new ProductPurchaseRequestException(
                    "One or more product does not exists"
            );
        }
        var sortedRequests = requestList
                .stream()
                .sorted(Comparator.comparing(ProductPurchaseRequest::productId))
                .toList();

        var sortedProducts = storedProducts
                .stream()
                .sorted(Comparator.comparing(Product::getId))
                .toList();

        var purchasedProducts = new ArrayList<ProductPurchaseResponse>();

        for(int i=0; i < sortedProducts.size(); i++){
            var product = sortedProducts.get(i);
            var productRequest = sortedRequests.get(i);
            if(product.getQuantity() < productRequest.quantity()) {
                throw new ProductPurchaseRequestException(
                        "Insufficient stock quantity for product ID:: "
                                + productRequest.productId()
                );
            }
            var newAvailableQuantity = product.getQuantity() - productRequest.quantity();
            product.setQuantity(newAvailableQuantity);
            productRepo.save(product);
            purchasedProducts.add(
                    mapper.toProductPurchaseResponse(product, productRequest.quantity())
            );
        }

        return purchasedProducts;
    }
}

package com.example.productservice.repo;

import com.example.productservice.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product,Long> {

    @Query("SELECT p FROM Product p WHERE id IN :productIds")
    List<Product> findByListOfIds(List<Long> productIds);
}

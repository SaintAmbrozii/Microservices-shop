package com.example.basketservice.repository;

import com.example.basketservice.domain.ProductDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductDetailsRepo extends JpaRepository<ProductDetails,Long> {

    List<ProductDetails> findByUser_id(Long id);
}

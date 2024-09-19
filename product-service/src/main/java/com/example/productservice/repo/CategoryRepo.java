package com.example.productservice.repo;

import com.example.productservice.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category,Long> {
}

package com.fanstatic.repository;

import com.fanstatic.model.ProductRating;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRatingRepository extends JpaRepository<ProductRating, Integer> {
}

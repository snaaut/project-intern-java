package com.project_intern.haibazo_shop.repository;

import com.project_intern.haibazo_shop.entity.Discount;
import com.project_intern.haibazo_shop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
   List<Discount> findByIdProductContains(Integer productId);

    List<Discount> findByIdProductContains(Long productId);
}
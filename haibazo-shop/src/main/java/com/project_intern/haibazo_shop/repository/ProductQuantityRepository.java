package com.project_intern.haibazo_shop.repository;

import com.project_intern.haibazo_shop.entity.Color;
import com.project_intern.haibazo_shop.entity.Product;
import com.project_intern.haibazo_shop.entity.ProductQuantity;
import com.project_intern.haibazo_shop.entity.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductQuantityRepository extends JpaRepository<ProductQuantity, Long> {

    @Query("SELECT pq FROM ProductQuantity pq WHERE pq.color.color = :color AND pq.size.size = :size AND pq.product.style = :style")
    List<ProductQuantity> findByColorSizeAndStyle(
            @Param("color") String color,
            @Param("size") String size,
            @Param("style") String style);

    List<ProductQuantity> findByProductId(Long productId);
    Optional<ProductQuantity> findByProductAndSizeAndColor(Product product, Size size, Color color);
}

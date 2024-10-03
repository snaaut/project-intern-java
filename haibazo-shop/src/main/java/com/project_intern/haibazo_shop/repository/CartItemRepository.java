package com.project_intern.haibazo_shop.repository;

import com.project_intern.haibazo_shop.entity.Cart;
import com.project_intern.haibazo_shop.entity.CartItem;
import com.project_intern.haibazo_shop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCartId(Long cartId);
    Optional<CartItem> findByCartAndProductAndSizeAndColor(Cart cart, Product product, String size, String color);
}

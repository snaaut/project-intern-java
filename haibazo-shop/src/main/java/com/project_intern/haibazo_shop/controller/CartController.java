package com.project_intern.haibazo_shop.controller;

import com.project_intern.haibazo_shop.dto.CartItemResponse;
import com.project_intern.haibazo_shop.entity.CartItem;
import com.project_intern.haibazo_shop.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;
    @PostMapping("/add-to-cart")
    public ResponseEntity<?> addProductToCart(
            @RequestParam Long userId,
            @RequestParam Long productId,
            @RequestParam String size,
            @RequestParam String color,
//            @RequestParam String style,
            @RequestParam int quantity) {
        try {
            List<CartItemResponse> cartItems = cartService.addProductToCart(userId, productId, size, color, quantity);
            return ResponseEntity.ok(cartItems);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/remain-server")
        public void remainServer() {
        //api rỗng để duy trì server
    }
}

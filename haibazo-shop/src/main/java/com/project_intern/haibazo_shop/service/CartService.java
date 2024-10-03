package com.project_intern.haibazo_shop.service;

import com.project_intern.haibazo_shop.dto.CartItemResponse;
import com.project_intern.haibazo_shop.entity.*;
import com.project_intern.haibazo_shop.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductQuantityRepository productQuantityRepository;

    @Autowired
    private SizeRepository sizeRepository;

    @Autowired
    private ColorRepository colorRepository;

    public List<CartItemResponse> addProductToCart(Long userId, Long productId, String sizeName, String colorName, int quantity) {

        Customer customer = customerRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Tạo mới giỏ hàng nếu chưa có
        Cart cart = cartRepository.findByCustomerId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setCustomer(customer);
                    return cartRepository.save(newCart);
                });

        // Tìm sản phẩm
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Tìm đối tượng size và color
        Size size = sizeRepository.findBySize(sizeName)
                .orElseThrow(() -> new RuntimeException("Size not available"));
        Color color = colorRepository.findByColor(colorName)
                .orElseThrow(() -> new RuntimeException("Color not available"));

        // Kiểm tra sản phẩm có size và color này có tồn tại k
        ProductQuantity productQuantity = productQuantityRepository.findByProductAndSizeAndColor(product, size, color)
                .orElseThrow(() -> new RuntimeException("Product not available"));

        // kiểm tra soos lượng
        if (quantity > productQuantity.getQuantity()) {
            throw new RuntimeException("Quantity demanded exceeds quantity in stock");
        }

        Optional<CartItem> existingCartItem = cartItemRepository.findByCartAndProductAndSizeAndColor(cart, product, sizeName, colorName);

        if (existingCartItem.isPresent()) {
            // Đã có trong giỏ hàng thì cộng thêm số lượng
            CartItem cartItem = existingCartItem.get();
            int newQuantity = cartItem.getQuantity() + quantity;
            if (newQuantity > productQuantity.getQuantity()) {
                throw new RuntimeException("Quantity demanded exceeds quantity in stock");
            }
            cartItem.setQuantity(newQuantity);
            cartItemRepository.save(cartItem);
        } else {
            // sản pham chưa có trong giỏ hàng thì them vào
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setSize(sizeName);
            newItem.setColor(colorName);
            newItem.setQuantity(quantity);
            cartItemRepository.save(newItem);
        }


//        return cartItemRepository.findByCartId(cart.getId());
        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());
        return cartItems.stream().map(cartItem -> {
            Product itemProduct = cartItem.getProduct();
            return new CartItemResponse(
                    itemProduct.getId(),
                    itemProduct.getName(),
                    cartItem.getSize(),
                    cartItem.getColor(),
                    cartItem.getQuantity()
            );
        }).collect(Collectors.toList());
    }
}

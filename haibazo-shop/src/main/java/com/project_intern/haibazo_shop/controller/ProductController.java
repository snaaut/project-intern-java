package com.project_intern.haibazo_shop.controller;

import com.project_intern.haibazo_shop.dto.ProductDetail;
import com.project_intern.haibazo_shop.dto.ProductResponse;
import com.project_intern.haibazo_shop.entity.Product;
import com.project_intern.haibazo_shop.exception.NotFoundException;
import com.project_intern.haibazo_shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/add-product")
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        Product newProduct = productService.addProduct(product);
        return ResponseEntity.ok(newProduct);
    }
    @GetMapping("/get-all-product")
    public List<ProductResponse> getAllProductsWithDiscount() {
        return productService.getAllProductsWithDiscount();
    }
    @GetMapping("/filter")
    public List<ProductResponse> filterProductsByColorSizeAndStype(
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String style) {
        return productService.filterProductsByColorSizeAndStyle(color, size, style);
    }

    @GetMapping("/detail/{productId}")
    public ResponseEntity<?> getProductDetail(@PathVariable Long productId) {
        try {
            ProductDetail productDetail = productService.getProductDetail(productId);
            return ResponseEntity.ok(productDetail);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}

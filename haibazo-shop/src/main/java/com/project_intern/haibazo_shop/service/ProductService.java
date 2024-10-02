package com.project_intern.haibazo_shop.service;

import com.project_intern.haibazo_shop.dto.ProductDetail;
import com.project_intern.haibazo_shop.dto.ProductResponse;
import com.project_intern.haibazo_shop.entity.Discount;
import com.project_intern.haibazo_shop.entity.Product;
import com.project_intern.haibazo_shop.entity.ProductQuantity;
import com.project_intern.haibazo_shop.entity.Review;
import com.project_intern.haibazo_shop.exception.NotFoundException;
import com.project_intern.haibazo_shop.repository.DiscountRepository;
import com.project_intern.haibazo_shop.repository.ProductQuantityRepository;
import com.project_intern.haibazo_shop.repository.ProductRepository;
import com.project_intern.haibazo_shop.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Autowired
    private ProductQuantityRepository productQuantityRepository;

    @Autowired
    private DiscountRepository discountRepository;

    public List<ProductResponse> getAllProductsWithDiscount() {
        List<Product> products = productRepository.findAll();

        return products.stream().map(product -> {
            // Tìm discount tương ứng với sản phẩm
            List<Discount> discounts = discountRepository.findByIdProductContains(Math.toIntExact(product.getId()));

            // Tính discountedPrice nếu có discount
            double discountedPrice = product.getOriginalPrice();
            if (!discounts.isEmpty()) {
                Discount discount = discounts.get(0); // Lấy discount đầu tiên (có thể tùy chỉnh theo logic của bạn)
                discountedPrice = product.getOriginalPrice() * (1 - discount.getDiscountPercentage());
            }

            // Tạo ProductResponse
            return ProductResponse.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .description(product.getDescription())
                    .stype(product.getStyle())
                    .images(product.getImages())
                    .originalPrice(product.getOriginalPrice())
                    .discountedPrice(discountedPrice)
                    .build();
        }).collect(Collectors.toList());
    }

    public List<ProductResponse> filterProductsByColorSizeAndStyle(String color, String size, String stype) {
        List<ProductQuantity> productQuantities = productQuantityRepository.findByColorSizeAndStyle(color, size, stype);

        return productQuantities.stream().map(productQuantity -> {
            var product = productQuantity.getProduct();

            // Tìm discount tương ứng với sản phẩm
            List<Discount> discounts = discountRepository.findByIdProductContains(Math.toIntExact(product.getId()));

            // Tính discountedPrice nếu có discount
            double discountedPrice = product.getOriginalPrice();
            if (!discounts.isEmpty()) {
                Discount discount = discounts.get(0); // Lấy discount đầu tiên (có thể tùy chỉnh theo logic của bạn)
                discountedPrice = product.getOriginalPrice() * (1 - discount.getDiscountPercentage());
            }



            // Tạo ProductResponse
            return ProductResponse.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .description(product.getDescription())
                    .stype(product.getStyle())
                    .images(product.getImages())
                    .originalPrice(product.getOriginalPrice())
                    .discountedPrice(discountedPrice)
                    .build();
        }).collect(Collectors.toList());
    }



    public ProductDetail getProductDetail(Long productId) {
        // Lấy thông tin đánh giá
        List<Review> reviews = reviewRepository.findByProductId(productId);
        int reviewCount = reviews.size();

        // Lấy thông tin size, color
        List<ProductQuantity> productQuantities = productQuantityRepository.findByProductId(productId);
        if (productQuantities.isEmpty()) {
            throw new NotFoundException("Product not found with ID: " + productId);
        }
        List<String> sizes = productQuantities.stream().map(pq -> pq.getSize().getSize()).distinct().collect(Collectors.toList());
        List<String> colors = productQuantities.stream().map(pq -> pq.getColor().getColor()).distinct().collect(Collectors.toList());

        // Lấy thông tin discount
        List<Discount> discounts = discountRepository.findByIdProductContains(productId);
        double discountedPrice = 0;
        long discountDurian = 0;
        if (!discounts.isEmpty()) {
            Discount discount = discounts.get(0);
            discountedPrice = productQuantities.get(0).getProduct().getOriginalPrice() * (1 - discount.getDiscountPercentage());

            // Tính số ngày còn lại của discount
            LocalDateTime now = LocalDateTime.now();
            discountDurian = Duration.between(now, discount.getDiscountDate()).toDays();
        }


        return ProductDetail.builder()
                .id(productQuantities.get(0).getProduct().getId())
                .name(productQuantities.get(0).getProduct().getName())
                .description(productQuantities.get(0).getProduct().getDescription())
                .style(productQuantities.get(0).getProduct().getStyle())
                .images(productQuantities.get(0).getProduct().getImages())
                .originalPrice(productQuantities.get(0).getProduct().getOriginalPrice())
                .discountedPrice(discountedPrice)
                .reviewCount(reviewCount)
                .discountTime(discountDurian)
                .sizes(sizes)
                .colors(colors)
                .build();
    }



    public Product addProduct(Product product) {
        return productRepository.save(product);
    }
}

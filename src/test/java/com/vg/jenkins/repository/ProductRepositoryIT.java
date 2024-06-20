package com.vg.jenkins.repository;

import com.vg.jenkins.model.Product;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@DisplayName("Product repository test")
@DataJpaTest
class ProductRepositoryIT {

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("Test find all products")
    @Test
    void findAll() {
        // Given
        Product product = Product.builder()
                .productId(1L)
                .name("Product 1")
                .description("test")
                .price(BigDecimal.valueOf(10.00))
                .createdAt(LocalDate.now())
                .build();

        Product product2 = Product.builder()
                .productId(2L)
                .name("Product 2")
                .description("test2")
                .price(BigDecimal.valueOf(10.00))
                .createdAt(LocalDate.now())
                .build();

        List<Product> productList = new ArrayList<>();
        productList.add(product);
        productList.add(product2);

        // When
        productRepository.findAll();

        // then
        assertThat(productList).hasSize(2);
    }

    @DisplayName("Product save test")
    @Test
    void save() {
        // Given
        Product product = Product.builder()
                .productId(1L)
                .name("Product 1")
                .description("test")
                .price(BigDecimal.valueOf(10.00))
                .createdAt(LocalDate.now())
                .build();

        // When
        Product savedProduct = productRepository.save(product);

        // Then
        assertThat(savedProduct.getProductId()).isNotNull();
        assertThat(savedProduct.getName()).isEqualTo("Product 1");
        assertThat(savedProduct.getDescription()).isEqualTo("test");
        assertThat(savedProduct.getPrice()).isEqualTo(BigDecimal.valueOf(10.00));
        assertThat(savedProduct.getCreatedAt()).isEqualTo(LocalDate.now());
        assertThat(savedProduct.getUpdatedAt()).isNull();
    }

    @DisplayName("Test delete product by id")
    @Test
    void delete() {
        // Given
        Product product = Product.builder()
                .productId(1L)
                .name("Product 1")
                .description("test")
                .price(BigDecimal.valueOf(10.00))
                .createdAt(LocalDate.now())
                .build();

        // When
        productRepository.deleteById(product.getProductId());

        // then
        Optional<Product> productOptional = productRepository.findById(product.getProductId());
        assertThat(productOptional).isNotPresent();
    }

    @DisplayName("Test find product by id")
    @Test
    void findById() {
        // Given
        Product product = Product.builder()
                .productId(1L)
                .name("Product 1")
                .description("test")
                .price(BigDecimal.valueOf(10.00))
                .createdAt(LocalDate.now())
                .build();

        productRepository.save(product);
        productRepository.save(product);

        // When
        Optional<Product> productOptional = productRepository.findById(product.getProductId());

        // then
        assertThat(productOptional).isPresent();
        assertThat(productOptional.get().getProductId()).isEqualTo(1L);
        assertThat(productOptional.get().getName()).isEqualTo("Product 1");
        assertThat(productOptional.get().getDescription()).isEqualTo("test");
        assertThat(productOptional.get().getPrice()).isEqualTo(BigDecimal.valueOf(10.00));
        assertThat(productOptional.get().getCreatedAt()).isEqualTo(LocalDate.now());
        assertThat(productOptional.get().getUpdatedAt()).isNull();
    }

}
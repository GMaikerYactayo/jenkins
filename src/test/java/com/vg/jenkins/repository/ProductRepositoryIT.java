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

    @DisplayName("Junit test for method findAll")
    @Test
    void givenProductsList_whenFindAll_thenReturnProductsList() {
        // Given - precondition or setup
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

        // When - action or the behavior that we are go int to test
        productRepository.findAll();

        // then - verify the output
        assertThat(productList).hasSize(2);
    }

    @DisplayName("Junit test for save method")
    @Test
    void giveProductObject_whenSave_thenReturnSavedProduct() {
        // Given - precondition or setup
        Product product = Product.builder()
                .productId(1L)
                .name("Product 1")
                .description("test")
                .price(BigDecimal.valueOf(10.00))
                .createdAt(LocalDate.now())
                .build();

        // When - action or the behavior that we are go int to test
        Product savedProduct = productRepository.save(product);

        // then - verify the output
        assertThat(savedProduct.getProductId()).isNotNull();
        assertThat(savedProduct.getName()).isEqualTo("Product 1");
        assertThat(savedProduct.getDescription()).isEqualTo("test");
        assertThat(savedProduct.getPrice()).isEqualTo(BigDecimal.valueOf(10.00));
        assertThat(savedProduct.getCreatedAt()).isEqualTo(LocalDate.now());
        assertThat(savedProduct.getUpdatedAt()).isNull();
    }

    @DisplayName("Junit test for deleteById method")
    @Test
    void givenProductObject_whenDelete_thenRemove() {
        // Given - precondition or setup
        Product product = Product.builder()
                .productId(1L)
                .name("Product 1")
                .description("test")
                .price(BigDecimal.valueOf(10.00))
                .createdAt(LocalDate.now())
                .build();

        // When - action or the behavior that we are go int to test
        productRepository.deleteById(product.getProductId());

        // then - verify the output
        Optional<Product> productOptional = productRepository.findById(product.getProductId());
        assertThat(productOptional).isNotPresent();
    }

    @DisplayName("Junit test for findById method")
    @Test
    void givenProductObject_whenFindById_thenReturnProductObject() {
        // Given - precondition or setup
        Product product = Product.builder()
                .productId(1L)
                .name("Product 1")
                .description("test")
                .price(BigDecimal.valueOf(10.00))
                .createdAt(LocalDate.now())
                .build();

        // When - action or the behavior that we are go int to test
        productRepository.findById(product.getProductId());

        // then - verify the output
        assertThat(product).isNotNull();
        assertThat(product.getProductId()).isEqualTo(1L);
        assertThat(product.getName()).isEqualTo("Product 1");
        assertThat(product.getDescription()).isEqualTo("test");
        assertThat(product.getPrice()).isEqualTo(BigDecimal.valueOf(10.00));
        assertThat(product.getCreatedAt()).isEqualTo(LocalDate.now());
        assertThat(product.getUpdatedAt()).isNull();
    }

}
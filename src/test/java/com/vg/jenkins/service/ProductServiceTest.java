package com.vg.jenkins.service;

import com.vg.jenkins.dto.ProductDTO;
import com.vg.jenkins.mapper.ProductMapper;
import com.vg.jenkins.model.Product;
import com.vg.jenkins.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@DisplayName("Product service test")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

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

        ProductDTO productDTO = ProductDTO.builder()
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();

        ProductDTO productDTO2 = ProductDTO.builder()
                .name(product2.getName())
                .description(product2.getDescription())
                .price(product2.getPrice())
                .createdAt(product2.getCreatedAt())
                .updatedAt(product2.getUpdatedAt())
                .build();

        given(productRepository.findAll()).willReturn(List.of(product, product2));
        given(productMapper.mapToDTO(product)).willReturn(productDTO);
        given(productMapper.mapToDTO(product2)).willReturn(productDTO2);

        // When
        List<ProductDTO> productDTOList = productService.findAll();

        // then
        assertThat(productDTOList).hasSize(2);
    }

    @DisplayName("Product save test")
    @Test
    void save() {
        // Given
        ProductDTO productDTO = ProductDTO.builder()
                .name("Product 1")
                .description("test")
                .price(BigDecimal.valueOf(10.00))
                .createdAt(LocalDate.now())
                .build();

        Product product = new Product();
        product.setProductId(1L);
        given(productMapper.mapToEntity(productDTO)).willReturn(product);
        given(productRepository.save(any(Product.class))).willReturn(product);
        given(productMapper.mapToDTO(product)).willReturn(productDTO);

        // When
        ProductDTO savedProduct = productService.save(productDTO);

        // Then
        assertThat(savedProduct.getName()).isEqualTo("Product 1");
        assertThat(savedProduct.getDescription()).isEqualTo("test");
        assertThat(savedProduct.getPrice()).isEqualTo(BigDecimal.valueOf(10.00));
        assertThat(savedProduct.getCreatedAt()).isEqualTo(LocalDate.now());
        assertThat(savedProduct.getUpdatedAt()).isNull();
    }

    @DisplayName("Product update test")
    @Test
    void update() {
        // Given
        Product product = Product.builder()
                .productId(1L)
                .name("Product 1")
                .description("test")
                .price(BigDecimal.valueOf(10.00))
                .createdAt(LocalDate.now())
                .build();

        ProductDTO productDTO = ProductDTO.builder()
                .name(product.getName())
                .description("update")
                .price(product.getPrice())
                .createdAt(product.getCreatedAt())
                .updatedAt(LocalDate.now())
                .build();

        given(productRepository.findById(product.getProductId())).willReturn(Optional.of(product));
        given(productRepository.save(product)).willReturn(product);
        given(productMapper.mapToDTO(product)).willReturn(productDTO);

        // When
        ProductDTO updatedProduct = productService.update(product.getProductId(), productDTO);

        // Then
        assertThat(updatedProduct.getName()).isEqualTo("Product 1");
        assertThat(updatedProduct.getDescription()).isEqualTo("update");
        assertThat(updatedProduct.getPrice()).isEqualTo(BigDecimal.valueOf(10.00));
        assertThat(updatedProduct.getCreatedAt()).isEqualTo(LocalDate.now());
        assertThat(updatedProduct.getUpdatedAt()).isEqualTo(LocalDate.now());
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

        given(productRepository.findById(product.getProductId())).willReturn(Optional.of(product));

        // When
        productService.delete(product.getProductId());

        // then
        verify(productRepository).deleteById(product.getProductId());
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

        ProductDTO productDTO = ProductDTO.builder()
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .createdAt(product.getCreatedAt())
                .updatedAt(LocalDate.now())
                .build();

        given(productRepository.findById(product.getProductId())).willReturn(Optional.of(product));
        given(productMapper.mapToDTO(product)).willReturn(productDTO);

        // When
        productService.findById(product.getProductId());

        // then
        assertThat(productDTO.getName()).isEqualTo("Product 1");
        assertThat(productDTO.getDescription()).isEqualTo("test");
        assertThat(productDTO.getPrice()).isEqualTo(BigDecimal.valueOf(10.00));
        assertThat(productDTO.getCreatedAt()).isEqualTo(LocalDate.now());
        assertThat(productDTO.getUpdatedAt()).isEqualTo(LocalDate.now());

    }
}
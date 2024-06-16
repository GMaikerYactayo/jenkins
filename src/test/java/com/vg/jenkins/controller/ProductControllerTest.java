package com.vg.jenkins.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vg.jenkins.dto.ProductDTO;
import com.vg.jenkins.model.Product;
import com.vg.jenkins.service.ProductService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.*;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;

@DisplayName("Product controller test")
@WebMvcTest
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    String endPoint = "/api/v1/products";

    @DisplayName("Test find all products")
    @Test
    void findAll() throws Exception {
        // Given
        List<ProductDTO> productDTOList = Arrays.asList(
                ProductDTO.builder()
                        .name("Product 1")
                        .description("test")
                        .price(BigDecimal.valueOf(10.00))
                        .createdAt(LocalDate.now())
                        .build(),
                ProductDTO.builder()
                        .name("Product 2")
                        .description("test2")
                        .price(BigDecimal.valueOf(10.00))
                        .createdAt(LocalDate.now())
                        .build()
        );

        given(productService.findAll()).willReturn(productDTOList);

        // When
        ResultActions response = mockMvc.perform(get(endPoint));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(productDTOList.size())))
                .andExpect(jsonPath("$[0].name", is("Product 1")))
                .andExpect(jsonPath("$[1].name", is("Product 2")));
        ;
    }

    @DisplayName("Product save test")
    @Test
    void save() throws Exception {
        // Given
        ProductDTO productDTO = ProductDTO.builder()
                .name("Product 1")
                .description("test")
                .price(BigDecimal.valueOf(10.00))
                .createdAt(LocalDate.now())
                .build();

        given(productService.save(productDTO)).willReturn(productDTO);

        // When
        ResultActions response = mockMvc.perform(post(endPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDTO)));

        // then
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", equalTo(productDTO.getName())))
                .andExpect(jsonPath("$.description", equalTo(productDTO.getDescription())))
                .andExpect(jsonPath("$.price", equalTo(productDTO.getPrice().doubleValue())))
                .andExpect(jsonPath("$.createdAt", equalTo(productDTO.getCreatedAt().toString())))
                .andExpect(jsonPath("$.updatedAt", nullValue()));
    }

    @DisplayName("Product update test")
    @Test
    void update() throws Exception {
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

        given(productService.update(product.getProductId(), productDTO)).willReturn(productDTO);

        // When
        ResultActions response = mockMvc.perform(put(endPoint + "/{id}", product.getProductId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDTO)));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(productDTO.getName())))
                .andExpect(jsonPath("$.description", equalTo(productDTO.getDescription())))
                .andExpect(jsonPath("$.price", equalTo(productDTO.getPrice().doubleValue())))
                .andExpect(jsonPath("$.createdAt", equalTo(productDTO.getCreatedAt().toString())))
                .andExpect(jsonPath("$.updatedAt", equalTo(productDTO.getUpdatedAt().toString())));
    }

    @DisplayName("Test delete product by id")
    @Test
    void deleteById() throws Exception {
        // Given
        Product product = Product.builder()
                .productId(1L)
                .name("Product 1")
                .description("test")
                .price(BigDecimal.valueOf(10.00))
                .createdAt(LocalDate.now())
                .build();

        willDoNothing().given(productService).delete(product.getProductId());

        // When
        ResultActions response = mockMvc.perform(delete(endPoint + "/{id}", product.getProductId()));

        // Then
        response.andExpect(status().isNoContent())
                .andDo(print());

    }

    @DisplayName("Test find product by id")
    @Test
    void findById() throws Exception {
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
                .build();

        given(productService.findById(product.getProductId())).willReturn(productDTO);

        // When
        ResultActions response = mockMvc.perform(get(endPoint + "/{id}", product.getProductId()));

        // Then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(productDTO.getName())))
                .andExpect(jsonPath("$.description", equalTo(productDTO.getDescription())))
                .andExpect(jsonPath("$.price", equalTo(productDTO.getPrice().doubleValue())))
                .andExpect(jsonPath("$.createdAt", equalTo(productDTO.getCreatedAt().toString())))
                .andExpect(jsonPath("$.updatedAt", nullValue()));
    }
}
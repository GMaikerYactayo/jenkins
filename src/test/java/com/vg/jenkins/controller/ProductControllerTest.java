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

    @DisplayName("Junit test for method findAllProducts")
    @Test
    void givenProductsList_whenFindAllProducts_thenReturnProductsList() throws Exception {
        // Given - precondition or setup
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

        // When - action or the behavior that we are go int to test
        ResultActions response = mockMvc.perform(get(endPoint));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(productDTOList.size())))
                .andExpect(jsonPath("$[0].name", is("Product 1")))
                .andExpect(jsonPath("$[1].name", is("Product 2")));
    }

    @DisplayName("Junit test for saveProduct method")
    @Test
    void giveProductObject_whenSaveProduct_thenReturnProductObject() throws Exception {
        // Given - precondition or setup
        ProductDTO productDTO = ProductDTO.builder()
                .name("Product 1")
                .description("test")
                .price(BigDecimal.valueOf(10.00))
                .createdAt(LocalDate.now())
                .build();

        given(productService.save(productDTO)).willReturn(productDTO);

        // When - action or the behavior that we are go int to test
        ResultActions response = mockMvc.perform(post(endPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDTO)));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", equalTo(productDTO.getName())))
                .andExpect(jsonPath("$.description", equalTo(productDTO.getDescription())))
                .andExpect(jsonPath("$.price", equalTo(productDTO.getPrice().doubleValue())))
                .andExpect(jsonPath("$.createdAt", equalTo(productDTO.getCreatedAt().toString())))
                .andExpect(jsonPath("$.updatedAt", nullValue()));
    }

    @DisplayName("Junit test for UpdateProduct method")
    @Test
    void givenProductObject_whenUpdateProduct_thenReturnUpdatedProduct() throws Exception {
        // Given - precondition or setup
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

        // When - action or the behavior that we are go int to test
        ResultActions response = mockMvc.perform(put(endPoint + "/{id}", product.getProductId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDTO)));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(productDTO.getName())))
                .andExpect(jsonPath("$.description", equalTo(productDTO.getDescription())))
                .andExpect(jsonPath("$.price", equalTo(productDTO.getPrice().doubleValue())))
                .andExpect(jsonPath("$.createdAt", equalTo(productDTO.getCreatedAt().toString())))
                .andExpect(jsonPath("$.updatedAt", equalTo(productDTO.getUpdatedAt().toString())));
    }

    @DisplayName("Junit test for deleteProduct method")
    @Test
    void givenProductObject_whenDeleteProduct_thenNothing() throws Exception {
        // Given - precondition or setup
        Product product = Product.builder()
                .productId(1L)
                .name("Product 1")
                .description("test")
                .price(BigDecimal.valueOf(10.00))
                .createdAt(LocalDate.now())
                .build();

        willDoNothing().given(productService).delete(product.getProductId());

        // When - action or the behavior that we are go int to test
        ResultActions response = mockMvc.perform(delete(endPoint + "/{id}", product.getProductId()));

        // then - verify the output
        response.andExpect(status().isNoContent())
                .andDo(print());

    }

    @DisplayName("Junit test for findProductById method")
    @Test
    void givenProductId_whenEmployeeFindById_thenReturnProductObject() throws Exception {
        // Given - precondition or setup
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

        // When - action or the behavior that we are go int to test
        ResultActions response = mockMvc.perform(get(endPoint + "/{id}", product.getProductId()));

        // then - verify the output
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(productDTO.getName())))
                .andExpect(jsonPath("$.description", equalTo(productDTO.getDescription())))
                .andExpect(jsonPath("$.price", equalTo(productDTO.getPrice().doubleValue())))
                .andExpect(jsonPath("$.createdAt", equalTo(productDTO.getCreatedAt().toString())))
                .andExpect(jsonPath("$.updatedAt", nullValue()));
    }
}
package com.vg.jenkins.service;

import com.vg.jenkins.dto.ProductDTO;

import java.util.List;

public interface ProductService {
    List<ProductDTO> findAll();
    ProductDTO save(ProductDTO product);
    ProductDTO update(long id, ProductDTO product);
    void delete(long id);
    ProductDTO findById(Long id);
}

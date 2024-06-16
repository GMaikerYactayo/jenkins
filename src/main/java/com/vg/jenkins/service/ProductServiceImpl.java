package com.vg.jenkins.service;

import com.vg.jenkins.dto.ProductDTO;
import com.vg.jenkins.mapper.ProductMapper;
import com.vg.jenkins.model.Product;
import com.vg.jenkins.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public ProductDTO save(ProductDTO product) {
        product.setCreatedAt(LocalDate.now());
        return Optional.of(product)
                .map(productMapper::mapToEntity)
                .map(productRepository::save)
                .map(productMapper::mapToDTO)
                .orElseThrow(() -> new IllegalArgumentException("Product entity cannot be null"));
    }

    @Override
    public ProductDTO update(long id, ProductDTO product) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    existingProduct.setName(product.getName());
                    existingProduct.setDescription(product.getDescription());
                    existingProduct.setPrice(product.getPrice());
                    existingProduct.setUpdatedAt(LocalDate.now());

                    Product updatedProduct = productRepository.save(existingProduct);

                    return productMapper.mapToDTO(updatedProduct);
                })
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));
    }

    @Override
    public void delete(long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        productOptional.ifPresentOrElse(
                product -> productRepository.deleteById(id),
                () -> { throw new RuntimeException("Product not found with ID: " + id); }
        );
    }


    @Override
    public List<ProductDTO> findAll() {
        return productRepository.findAll().stream()
                .map(productMapper::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDTO findById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Product with ID " + id + " not found"));
    }
}
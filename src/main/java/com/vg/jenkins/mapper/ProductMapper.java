package com.vg.jenkins.mapper;

import com.vg.jenkins.dto.ProductDTO;
import com.vg.jenkins.model.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDTO mapToDTO(Product product);
    Product mapToEntity(ProductDTO productDTO);

}

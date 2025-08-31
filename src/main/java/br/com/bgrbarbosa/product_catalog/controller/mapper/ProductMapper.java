package br.com.bgrbarbosa.product_catalog.controller.mapper;

import br.com.bgrbarbosa.product_catalog.model.Product;
import br.com.bgrbarbosa.product_catalog.model.dto.ProductDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product parseToEntity(ProductDTO dto);

    ProductDTO parseToDto(Product entity);

    List<ProductDTO> parseToListDTO(List<Product>list);

    List<Product> parseToList(List<ProductDTO>list);
}

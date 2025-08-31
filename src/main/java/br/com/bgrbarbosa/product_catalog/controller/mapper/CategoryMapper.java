package br.com.bgrbarbosa.product_catalog.controller.mapper;

import br.com.bgrbarbosa.product_catalog.model.Category;
import br.com.bgrbarbosa.product_catalog.model.dto.CategoryDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category parseToEntity(CategoryDTO dto);

    CategoryDTO parseToDto(Category entity);

    List<CategoryDTO> parseToListDTO(List<Category>list);
}

package br.com.bgrbarbosa.product_catalog.service;

import br.com.bgrbarbosa.product_catalog.model.Category;
import br.com.bgrbarbosa.product_catalog.model.Product;
import br.com.bgrbarbosa.product_catalog.model.dto.CategoryDTO;

import java.util.List;
import java.util.UUID;

public interface CategoryService {

    Category insert(Category category);

    List<Category> findAll();

    Category findById(UUID uuid);

    void delete(UUID uuid);

    Category update(Category category);
}

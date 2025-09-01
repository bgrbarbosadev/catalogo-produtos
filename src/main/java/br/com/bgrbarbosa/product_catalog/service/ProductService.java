package br.com.bgrbarbosa.product_catalog.service;

import br.com.bgrbarbosa.product_catalog.model.Product;
import br.com.bgrbarbosa.product_catalog.specification.filter.ProductFilter;
import org.springframework.data.domain.Pageable;


import java.util.List;
import java.util.UUID;

public interface ProductService {

    Product insert(Product product);

    List<Product> findAll(Pageable page, ProductFilter filter);

    Product findById(UUID uuid);

    void delete(UUID uuid);

    Product update(Product product);
}

package br.com.bgrbarbosa.product_catalog.service;

import br.com.bgrbarbosa.product_catalog.model.Product;


import java.util.List;
import java.util.UUID;

public interface ProductService {

    Product insert(Product product);

    List<Product> findAll();

    Product findById(UUID uuid);

    void delete(UUID uuid);

    Product update(Product product);
}

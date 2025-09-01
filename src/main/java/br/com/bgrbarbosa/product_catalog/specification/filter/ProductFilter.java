package br.com.bgrbarbosa.product_catalog.specification.filter;

import br.com.bgrbarbosa.product_catalog.model.Category;
import br.com.bgrbarbosa.product_catalog.model.Product;
import static br.com.bgrbarbosa.product_catalog.specification.ProductSpec.*;

import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class ProductFilter {

    private UUID uuid;
    private String nameProduct;
    private String descriptionProduct;
    private Double priceProduct;
    private String urlProduct;
    private LocalDate dtCreated;
    private LocalDate dtUpdated;
    private String category;

    public Specification<Product> toSpecification() {
        Specification<Product> spec = Specification.where(null);

        if (!ObjectUtils.isEmpty(nameProduct)) {
            spec = spec.and(searchNameProduct(nameProduct));
        }

        if (!ObjectUtils.isEmpty(descriptionProduct)) {
            spec = spec.and(searchDescriptionProduct(descriptionProduct));
        }

        if (!ObjectUtils.isEmpty(priceProduct)) {
            spec = spec.and(searchPriceProduct(priceProduct));
        }

        if (!ObjectUtils.isEmpty(category)) {
            spec = spec.and(searchCategoryName(category));
        }

        return spec;
    }


}

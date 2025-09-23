package dev.ioannis.anemosparts.mapper;

import dev.ioannis.anemosparts.domain.Product;
import dev.ioannis.anemosparts.domain.entity.ProductEntity;

import java.util.HashSet;

public class ProductMapper {
    public static Product convertToProduct(ProductEntity entity) {
        return new Product(entity.getProductId(), entity.getProductName(), entity.getProductDescription(), entity.getProductPrice(), entity.getProductStock(), new HashSet<>());
    }

//    public static ProductEntity convertToProductEntity(Product model) {
//
//    }
}

package dev.ioannis.anemosparts.persistance;

import org.springframework.data.repository.CrudRepository;
import dev.ioannis.anemosparts.domain.entity.ProductEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo extends CrudRepository<ProductEntity, Long> {
}

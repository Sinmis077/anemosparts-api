package dev.ioannis.anemosparts.persistance;

import org.springframework.data.repository.CrudRepository;
import dev.ioannis.anemosparts.entity.BrandEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepo extends CrudRepository<BrandEntity, String> {
}

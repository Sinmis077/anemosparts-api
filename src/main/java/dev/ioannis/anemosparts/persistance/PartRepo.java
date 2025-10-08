package dev.ioannis.anemosparts.persistance;

import dev.ioannis.anemosparts.entity.PartEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartRepo extends CrudRepository<PartEntity, Long> {
}

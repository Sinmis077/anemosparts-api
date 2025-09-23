package dev.ioannis.anemosparts.persistance;

import org.springframework.data.repository.CrudRepository;
import dev.ioannis.anemosparts.domain.entity.ModelEntity;
import dev.ioannis.anemosparts.domain.entity.ModelEntityId;
import org.springframework.stereotype.Repository;

@Repository
public interface ModelRepo extends CrudRepository<ModelEntity, ModelEntityId> {}

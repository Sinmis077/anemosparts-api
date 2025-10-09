package dev.ioannis.anemosparts.persistance;

import dev.ioannis.anemosparts.entity.ModelEntity;
import dev.ioannis.anemosparts.entity.ModelEntityId;
import dev.ioannis.anemosparts.entity.PartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PartRepo extends JpaRepository<PartEntity, Long> {
    List<PartEntity> findByName(String name);
    List<PartEntity> findByModels(List<ModelEntity> models);
    List<PartEntity> findByModels(ModelEntityId modelId);
    Optional<PartEntity> findByIsbn(String ISBN);
    Optional<PartEntity> findByPartNumber(String partNumber);
}

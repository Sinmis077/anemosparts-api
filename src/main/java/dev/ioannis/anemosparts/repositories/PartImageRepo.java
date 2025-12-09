package dev.ioannis.anemosparts.repositories;

import dev.ioannis.anemosparts.entities.PartImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartImageRepo extends JpaRepository<PartImage, Integer> {
    void deleteAllByPartId(long partId);
}

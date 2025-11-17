package dev.ioannis.anemosparts.repositories;

import dev.ioannis.anemosparts.entities.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PartRepo extends JpaRepository<Part, Long>, JpaSpecificationExecutor<Part> {}

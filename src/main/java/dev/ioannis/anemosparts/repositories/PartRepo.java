package dev.ioannis.anemosparts.repositories;

import dev.ioannis.anemosparts.entities.OemNumber;
import dev.ioannis.anemosparts.entities.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PartRepo extends JpaRepository<Part, Long>, JpaSpecificationExecutor<Part> {}

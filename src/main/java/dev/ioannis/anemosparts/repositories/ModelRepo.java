package dev.ioannis.anemosparts.repositories;

import dev.ioannis.anemosparts.entities.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ModelRepo extends JpaRepository<Model, Long>, JpaSpecificationExecutor<Model> {}

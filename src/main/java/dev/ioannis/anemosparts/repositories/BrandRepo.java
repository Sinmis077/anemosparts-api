package dev.ioannis.anemosparts.repositories;

import dev.ioannis.anemosparts.entities.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepo extends JpaRepository<Brand, Long> {}

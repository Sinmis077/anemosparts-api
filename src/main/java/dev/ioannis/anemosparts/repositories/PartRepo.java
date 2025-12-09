package dev.ioannis.anemosparts.repositories;

import dev.ioannis.anemosparts.entities.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PartRepo extends JpaRepository<Part, Long>, JpaSpecificationExecutor<Part> {
    @Query("SELECT p.quantity FROM Part p WHERE p.id = :id")
    Integer getQuantityById(Long id);

    @Modifying
    @Query("UPDATE Part p SET p.quantity = :quantity where p.id = :partId")
    Integer updateQuantityById(Long partId, Integer quantity);
}

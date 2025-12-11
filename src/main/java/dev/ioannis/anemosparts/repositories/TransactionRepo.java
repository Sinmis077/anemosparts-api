package dev.ioannis.anemosparts.repositories;

import dev.ioannis.anemosparts.entities.PartTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepo extends JpaRepository<PartTransaction, Long> {
}

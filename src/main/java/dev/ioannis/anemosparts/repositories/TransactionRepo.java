package dev.ioannis.anemosparts.repositories;

import dev.ioannis.anemosparts.entities.PartTransaction;
import dev.ioannis.anemosparts.enums.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepo extends JpaRepository<PartTransaction, Long> {

    @Query("SELECT COALESCE(sum(pt.quantity), 0) as sum FROM PartTransaction as pt WHERE pt.status = 0 and pt.part.id = :partId")
    Integer sumOfQuantityOnHoldById(Long partId);
}

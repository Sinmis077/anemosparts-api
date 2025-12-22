package dev.ioannis.anemosparts.repositories;

import dev.ioannis.anemosparts.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepo extends JpaRepository<Account, UUID> {
    Boolean existsByEmail(String email);

    Optional<Account> findByEmail(String email);
}

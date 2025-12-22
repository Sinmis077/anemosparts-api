package dev.ioannis.anemosparts.repositories;

import dev.ioannis.anemosparts.entities.Account;
import dev.ioannis.anemosparts.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepo extends JpaRepository<Address, Long> {

}

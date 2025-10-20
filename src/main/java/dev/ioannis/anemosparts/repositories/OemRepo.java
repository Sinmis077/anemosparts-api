package dev.ioannis.anemosparts.repositories;

import dev.ioannis.anemosparts.entities.OemNumber;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

public interface OemRepo extends Repository<OemNumber, Long> {
    OemNumber findByNumber(String number);

    boolean existsByNumber(String number);
}

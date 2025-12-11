package dev.ioannis.anemosparts.repositories;

import dev.ioannis.anemosparts.entities.OemNumber;
import org.springframework.data.repository.Repository;

@org.springframework.stereotype.Repository
public interface OemRepo extends Repository<OemNumber, Long> {
    OemNumber save(OemNumber oemNumber);

    OemNumber findByNumber(String number);

    boolean existsByNumber(String number);
}

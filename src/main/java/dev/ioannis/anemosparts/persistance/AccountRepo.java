package dev.ioannis.anemosparts.persistance;

import dev.ioannis.anemosparts.domain.entity.AccountEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface AccountRepo extends CrudRepository<AccountEntity, String> {
}

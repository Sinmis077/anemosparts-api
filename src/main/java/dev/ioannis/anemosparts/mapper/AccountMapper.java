package dev.ioannis.anemosparts.mapper;

import dev.ioannis.anemosparts.domain.Account;
import dev.ioannis.anemosparts.domain.entity.AccountEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AccountMapper {
    AccountMapper INSTANCE = Mappers.getMapper( AccountMapper.class );

    AccountEntity toAccountEntity(Account account);
    Account toAccount(AccountEntity accountEntity);
}

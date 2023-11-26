package penguin.wordbook.mapper;

import static penguin.wordbook.controller.dto.AccountDto.*;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import penguin.auth.model.entity.Account;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface AccountMapper {
    @Mapping(target = "accountId", ignore = true)
    Account map(AccountCreateDto accountCreateDto);

    @Mapping(target = "password", ignore = true)
    Account map(AccountInfoDto accountInfoDto);

    Account map(AccountUpdateDto accountUpdateDto);

    AccountInfoDto map(Account account);
}

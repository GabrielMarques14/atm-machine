package com.zw.atm.rest.dto.mapper;

import com.zw.atm.repo.entity.Account;
import com.zw.atm.rest.dto.AccountAuthDTO;
import com.zw.atm.rest.dto.AccountDTO;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AccountMapper {

	public Account toAccount(AccountAuthDTO accountDTO) {
		Optional<AccountAuthDTO> dto = Optional.ofNullable(accountDTO);
		return dto.isPresent() ?
				new Account(dto.get().getAccountNumber(), dto.get().getPin()) : new Account();
	}

	public AccountDTO toAccountDTO(Optional<Account> account) {
		return account.isPresent() ?
				new AccountDTO(account.get().getNumber(), account.get().getBalance(), account.get().getOverdraft())
				: new AccountDTO();
	}

}

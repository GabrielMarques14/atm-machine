package com.zw.atm.service;

import com.zw.atm.exception.InvalidRequestException;
import com.zw.atm.repo.dao.AccountDAO;
import com.zw.atm.repo.entity.Account;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.zw.atm.service.validator.AccountPreValidation.*;
import static com.zw.atm.service.validator.AccountPreValidation.PreValidationResult.SUCCESS;
import static com.zw.atm.service.validator.AccountPreValidation.hasEnoughFunds;
import static com.zw.atm.service.validator.AccountPreValidation.hasIncorrectPIN;

@AllArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {

	private AccountDAO accountDAO;

	@Override
	public Optional<Account> findAccountByNumber(long accountNumber) {
		return accountDAO.findById(accountNumber);
	}

	@Override
	public void updateBalance(Account account, long quantity) {
		Optional<Account> acc = accountDAO.findById(account.getNumber());

		acc.ifPresent(a -> {
			if (a.getBalance() >= quantity
					|| a.getBalance() + a.getOverdraft() >= quantity) {
				a.setBalance(a.getBalance() - quantity);
			}

			accountDAO.save(a);
		});
	}

	@Override
	public void isValidWithdrawalRequest(Account account, long quantity) {
		Optional<Account> acc = accountDAO.findById(account.getNumber());

		PreValidationResult result = hasIncorrectPIN(account).
				and(hasEnoughFunds(quantity)).apply(acc.get());

		if (result != SUCCESS) {
			throw new InvalidRequestException(result.getDescription());
		}
	}

	@Override
	public void isValidBalanceCheck(Account account) {
		Optional<Account> acc = accountDAO.findById(account.getNumber());

		PreValidationResult result = hasIncorrectPIN(account).apply(acc.get());

		if (result != SUCCESS) {
			throw new InvalidRequestException(result.getDescription());
		}
	}

}

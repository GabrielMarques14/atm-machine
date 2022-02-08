package com.zw.atm.service;

import com.zw.atm.repo.entity.Account;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
public interface AccountService {

	Optional<Account> findAccountByNumber(long accountNumber);

	void updateBalance(Account account, long quantity);

	void isValidWithdrawalRequest(Account account, long quantity);

	void isValidBalanceCheck(Account account);

}

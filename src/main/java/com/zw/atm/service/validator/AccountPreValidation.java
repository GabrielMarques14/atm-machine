package com.zw.atm.service.validator;

import com.zw.atm.repo.entity.Account;

import java.util.function.Function;

import static com.zw.atm.service.validator.AccountPreValidation.*;
import static com.zw.atm.service.validator.AccountPreValidation.PreValidationResult.*;

public interface AccountPreValidation extends Function<Account, PreValidationResult> {

	static AccountPreValidation hasIncorrectPIN(Account accOther) {
		return acc -> acc.getPin() == accOther.getPin() ? SUCCESS : INCORRECT_PIN;
	}

	static AccountPreValidation hasEnoughFunds(long quantity) {
		return acc -> acc.getBalance() + acc.getOverdraft() >= quantity ? SUCCESS : NOT_ENOUGH_FUNDS;
	}

	default AccountPreValidation and(AccountPreValidation other) {
		return trs -> {
			PreValidationResult result = this.apply(trs);
			return result.equals(SUCCESS) ? other.apply(trs) : result;
		};
	}

	enum PreValidationResult {
		SUCCESS(""),
		INCORRECT_PIN("Incorrect PIN"),
		NOT_ENOUGH_FUNDS("Not enough funds in your account");

		private String description;

		PreValidationResult(String description) {
			this.description = description;
		}

		public String getDescription() {
			return description;
		}

	}

}

package com.zw.atm.service.validator;

import java.util.function.Function;

import static com.zw.atm.service.validator.FundPreValidation.*;
import static com.zw.atm.service.validator.FundPreValidation.PreValidationResult.*;

public interface FundPreValidation extends Function<Long, PreValidationResult> {

	static FundPreValidation isValidAmount() {
		return quantity -> quantity > 0 && quantity % 5 == 0 ? SUCCESS : INVALID_AMOUNT;
	}

	static FundPreValidation hasEnoughFunds(long funds) {
		return quantity -> quantity <= funds ? SUCCESS : NOT_ENOUGH_FUNDS;
	}

	default FundPreValidation and(FundPreValidation other) {
		return trs -> {
			PreValidationResult result = this.apply(trs);
			return result.equals(SUCCESS) ? other.apply(trs) : result;
		};
	}

	enum PreValidationResult {
		SUCCESS(""),
		INVALID_AMOUNT("Invalid amount: only multiples of 5 are allowed"),
		NOT_ENOUGH_FUNDS("Not enough funds in the ATM Machine");

		private String description;

		PreValidationResult(String description) {
			this.description = description;
		}

		public String getDescription() {
			return description;
		}

	}

}

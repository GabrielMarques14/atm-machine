package com.zw.atm.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class WithdrawalDTO {

	private AccountDTO accountBalance;
	private List<FundDTO> funds;
}

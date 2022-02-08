package com.zw.atm.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {

	private long number;
	private long balance;
	private long overdraft;

}

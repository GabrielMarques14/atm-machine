package com.zw.atm.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountAuthDTO {

	private long accountNumber;
	private int pin;
	private long quantity;

}

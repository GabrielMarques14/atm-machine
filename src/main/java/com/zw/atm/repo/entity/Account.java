package com.zw.atm.repo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ZW_ACCOUNT")
public class Account {

	@Id
	@Column(name = "ACC_NUMBER")
	private long number;

	@Column(name = "ACC_PIN")
	private int pin;

	@Setter
	@Column(name = "ACC_BALANCE")
	private long balance;

	@Column(name = "ACC_OVERDRAFT")
	private long overdraft;

	public Account(long number, int pin) {
		this.number = number;
		this.pin = pin;
	}

}

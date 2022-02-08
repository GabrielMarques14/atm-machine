package com.zw.atm.repo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "ZW_ATM_STOCK")
public class Fund {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "STOCK_ID")
	private long id;

	@Column(name = "STOCK_BILL", unique = true)
	private int bill;

	@Setter
	@Column(name = "STOCK_QUANTITY")
	private long quantity;

	public Fund(int bill, long quantity) {
		this.bill = bill;
		this.quantity = quantity;
	}

}

package com.zw.atm;

import com.zw.atm.repo.dao.AccountDAO;
import com.zw.atm.repo.dao.FundDAO;
import com.zw.atm.repo.entity.Account;
import com.zw.atm.repo.entity.Fund;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class Runner implements CommandLineRunner {

	private FundDAO atmFundDAO;
	private AccountDAO accountDAO;

	@Override
	public void run(String... args) throws Exception {
		atmFundDAO.saveAll(List.of(
				new Fund(50, 10),
				new Fund(20, 30),
				new Fund(10, 30),
				new Fund(5, 20)
		));

		accountDAO.saveAll(List.of(
				new Account(123456789, 1234, 800, 200),
				new Account(987654321, 4321, 1230, 150)
		));
	}

}

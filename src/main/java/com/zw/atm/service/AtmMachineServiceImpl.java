package com.zw.atm.service;

import com.zw.atm.repo.entity.Account;
import com.zw.atm.repo.entity.Fund;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@AllArgsConstructor
@Service
public class AtmMachineServiceImpl implements AtmMachineService {

	private FundService fundService;
	private AccountService accountService;

	@Override
	public List<Fund> withdrawFunds(Account account, long quantity) {
		List<Fund> fundList = fundService.findAll();

		long remaining = quantity;
		List<Fund> funds = new ArrayList<>();
		for (Fund fund : fundList) {
			if (fund.getBill() > remaining) continue;
			else if (fund.getQuantity() == 0) continue;

			long nBillsNeeded = remaining / fund.getBill();
			long nBillsPossible = nBillsNeeded > fund.getQuantity() ? fund.getQuantity() : nBillsNeeded;
			remaining -= nBillsPossible * fund.getBill();

			funds.add(new Fund(fund.getBill(), nBillsPossible));
			fundService.updateStock(fund, nBillsPossible);

			if (remaining == 0) break;
		}

		accountService.updateBalance(account, quantity);

		return funds;
	}

}

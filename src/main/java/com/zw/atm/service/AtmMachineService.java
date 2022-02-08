package com.zw.atm.service;

import com.zw.atm.repo.entity.Account;
import com.zw.atm.repo.entity.Fund;

import java.util.List;

public interface AtmMachineService {

	List<Fund> withdrawFunds(Account account, long quantity);

}

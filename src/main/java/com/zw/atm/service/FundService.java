package com.zw.atm.service;

import com.zw.atm.repo.entity.Fund;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface FundService {

	List<Fund> findAll();

	Fund updateStock(Fund fund, long quantity);

	void isValidRequest(long quantity);

}

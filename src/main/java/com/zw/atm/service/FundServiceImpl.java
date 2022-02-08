package com.zw.atm.service;

import com.zw.atm.exception.InvalidRequestException;
import com.zw.atm.repo.dao.FundDAO;
import com.zw.atm.repo.entity.Fund;
import com.zw.atm.service.validator.FundPreValidation.PreValidationResult;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.zw.atm.service.validator.FundPreValidation.*;
import static com.zw.atm.service.validator.FundPreValidation.PreValidationResult.SUCCESS;

@AllArgsConstructor
@Service
public class FundServiceImpl implements FundService {

	private FundDAO fundDAO;

	@Override
	public List<Fund> findAll() {
		List<Fund> fundList = fundDAO.findAll(Sort.by("bill").descending());

		return fundList;
	}

	@Override
	public Fund updateStock(Fund fund, long quantity) {
		fund.setQuantity(fund.getQuantity() - quantity);
		return fundDAO.save(fund);
	}

	@Override
	public void isValidRequest(long quantity) {
		long availableFunds = fundDAO.findAll()
				.stream()
				.collect(Collectors.summingLong(s -> s.getBill() * s.getQuantity()));

		PreValidationResult result = isValidAmount()
				.and(hasEnoughFunds(availableFunds)).apply(quantity);

		if (result != SUCCESS) {
			throw new InvalidRequestException(result.getDescription());
		}
	}

}

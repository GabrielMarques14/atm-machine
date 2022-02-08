package com.zw.atm.rest.dto.mapper;

import com.zw.atm.repo.entity.Fund;
import com.zw.atm.rest.dto.FundDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class FundMapper {

	public FundDTO toFundDTO(Fund fund) {
		Optional<Fund> stockOptional = Optional.ofNullable(fund);
		return stockOptional.isPresent() ?
				new FundDTO(fund.getBill(), fund.getQuantity()) : new FundDTO();
	}

	public List<FundDTO> toFundDTOList(List<Fund> fundList) {
		List<FundDTO> fundDTOList = new ArrayList<>();
		fundList.stream().forEach(f -> fundDTOList.add(toFundDTO(f)));

		return fundDTOList;
	}

}

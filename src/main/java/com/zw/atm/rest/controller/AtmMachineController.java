package com.zw.atm.rest.controller;

import com.zw.atm.exception.InvalidRequestException;
import com.zw.atm.repo.entity.Account;
import com.zw.atm.repo.entity.Fund;
import com.zw.atm.rest.dto.AccountAuthDTO;
import com.zw.atm.rest.dto.AccountDTO;
import com.zw.atm.rest.dto.FundDTO;
import com.zw.atm.rest.dto.WithdrawalDTO;
import com.zw.atm.rest.dto.mapper.AccountMapper;
import com.zw.atm.rest.dto.mapper.FundMapper;
import com.zw.atm.service.AccountService;
import com.zw.atm.service.AtmMachineService;
import com.zw.atm.service.FundService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/atm")
public class AtmMachineController {

	private AccountService accountService;
	private FundService fundService;
	private AtmMachineService atmMachineService;
	private AccountMapper accountMapper;
	private FundMapper fundMapper;

	@PostMapping("/withdrawal")
	public ResponseEntity<WithdrawalDTO> withdrawFunds(@RequestBody(required = true) AccountAuthDTO accountAuthDTO) {
		Account acc = accountMapper.toAccount(accountAuthDTO);

		try {
			accountService.isValidWithdrawalRequest(acc, accountAuthDTO.getQuantity());
			fundService.isValidRequest(accountAuthDTO.getQuantity());
		} catch (InvalidRequestException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}

		List<Fund> fundList = atmMachineService.withdrawFunds(acc, accountAuthDTO.getQuantity());

		List<FundDTO> fundDTOList = fundMapper.toFundDTOList(fundList);
		AccountDTO accDTO = accountMapper.toAccountDTO(accountService.findAccountByNumber(acc.getNumber()));
		return new ResponseEntity<>(new WithdrawalDTO(accDTO, fundDTOList), HttpStatus.OK);
	}

	@GetMapping("/balance")
	public ResponseEntity<AccountDTO> checkBalance(@RequestBody(required = true) AccountAuthDTO accountAuthDTO) {
		Account acc = accountMapper.toAccount(accountAuthDTO);

		try {
			accountService.isValidBalanceCheck(acc);
		} catch (InvalidRequestException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}

		AccountDTO accDTO = accountMapper.toAccountDTO(accountService.findAccountByNumber(acc.getNumber()));
		return new ResponseEntity<>(accDTO, HttpStatus.OK);
	}

}

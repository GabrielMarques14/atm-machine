package com.zw.atm.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zw.atm.exception.InvalidRequestException;
import com.zw.atm.repo.entity.Account;
import com.zw.atm.repo.entity.Fund;
import com.zw.atm.rest.dto.AccountAuthDTO;
import com.zw.atm.rest.dto.AccountDTO;
import com.zw.atm.rest.dto.FundDTO;
import com.zw.atm.rest.dto.mapper.AccountMapper;
import com.zw.atm.rest.dto.mapper.FundMapper;
import com.zw.atm.service.AccountService;
import com.zw.atm.service.AtmMachineService;
import com.zw.atm.service.FundService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class AtmMachineControllerUnitTest {

	@Autowired
	private MockMvc mockMvc;

	@Mock
	private AtmMachineService atmMachineService;

	@Mock
	private AccountService accountService;

	@Mock
	private FundService fundService;

	@Mock
	private AccountMapper accountMapper;

	@Mock
	private FundMapper fundMapper;

	@InjectMocks
	private AtmMachineController atmMachineController;

	private Optional<Account> validAccount;
	private AccountAuthDTO accountAuthDTO;
	private AccountDTO accountDTO;
	private List<Fund> fundList;
	private List<FundDTO> fundDTOList;

	@BeforeEach
	public void setUp() {
		validAccount = Optional.of(new Account(123456789, 1234, 800, 200));
		accountAuthDTO = new AccountAuthDTO(123456789, 1234, 200);
		accountDTO = new AccountDTO(123456789, 1234, 200);
		fundList = List.of(
				new Fund(50, 10),
				new Fund(20, 30),
				new Fund(10, 30),
				new Fund(5, 20)
		);
		fundDTOList = List.of(
				new FundDTO(50, 10),
				new FundDTO(20, 30),
				new FundDTO(10, 30),
				new FundDTO(5, 20)
		);
		mockMvc = MockMvcBuilders.standaloneSetup(atmMachineController).build();
	}

	@AfterEach
	public void tearDown() {
		validAccount = null;
		accountAuthDTO = null;
		fundList = null;
		fundDTOList = null;
	}

	@Test
	@DisplayName("Withdraw funds")
	void testWithdrawWithSuccess() throws Exception {
		//  given
		long number = 123456789;
		long quantity = 200;

		//  when
		when(accountMapper.toAccount(any())).thenReturn(validAccount.get());
		when(atmMachineService.withdrawFunds(any(), eq(quantity))).thenReturn(fundList);
		when(fundMapper.toFundDTOList(any())).thenReturn(fundDTOList);
		when(accountService.findAccountByNumber(eq(number))).thenReturn(validAccount);
		when(accountMapper.toAccountDTO(any())).thenReturn(accountDTO);

		//  then
		mockMvc.perform(MockMvcRequestBuilders
						.post("/api/v1/atm/withdrawal")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectToJSON(accountAuthDTO)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print());
		verify(accountMapper, times(1)).toAccount(any());
		verify(atmMachineService, times(1)).withdrawFunds(any(), eq(quantity));
		verify(fundMapper, times(1)).toFundDTOList(any());
		verify(accountService, times(1)).findAccountByNumber(eq(number));
		verify(accountMapper, times(1)).toAccountDTO(any());
	}

	@Test
	@DisplayName("Tries to checks account balance with invalid data")
	void testWithdrawWithInvalidData() throws Exception {
		//  given
		long quantity = 200;
		String errorMsg = "error";

		//  then
		doThrow(new InvalidRequestException(errorMsg)).when(fundService).isValidRequest(eq(quantity));
		mockMvc.perform(MockMvcRequestBuilders
						.post("/api/v1/atm/withdrawal")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectToJSON(accountAuthDTO)))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andDo(MockMvcResultHandlers.print());
	}

	@Test
	@DisplayName("Checks account balance")
	void testCheckBalanceWithSuccess() throws Exception {
		//  given
		long number = 123456789;

		//  when
		when(accountMapper.toAccount(any())).thenReturn(validAccount.get());
		when(accountService.findAccountByNumber(eq(number))).thenReturn(validAccount);
		when(accountMapper.toAccountDTO(any())).thenReturn(accountDTO);


		//  then
		mockMvc.perform(MockMvcRequestBuilders
						.get("/api/v1/atm/balance")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectToJSON(accountAuthDTO)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andDo(MockMvcResultHandlers.print());
		verify(accountMapper, times(1)).toAccount(any());
		verify(accountService, times(1)).findAccountByNumber(eq(number));
		verify(accountMapper, times(1)).toAccountDTO(any());
	}

	@Test
	@DisplayName("Tries to checks account balance with incorrect PIN")
	void testCheckBalanceWithIncorrectPin() throws Exception {
		//  given
		String errorMsg = "error";

		//  then
		doThrow(new InvalidRequestException(errorMsg)).when(accountService).isValidBalanceCheck(any());
		mockMvc.perform(MockMvcRequestBuilders
						.get("/api/v1/atm/balance")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectToJSON(accountAuthDTO)))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andDo(MockMvcResultHandlers.print());
	}

	public static String objectToJSON(Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}

package com.zw.atm.service;

import com.zw.atm.repo.entity.Account;
import com.zw.atm.repo.entity.Fund;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class AtmMachineServiceUnitTest {

	@Mock
	private FundService fundService;

	@Mock
	private AccountService accountService;

	@Autowired
	@InjectMocks
	private AtmMachineServiceImpl atmMachineService;

	private Optional<Account> validAccount;
	private Fund fund;
	private List<Fund> fundList;

	@BeforeEach
	public void setUp() {
		validAccount = Optional.of(new Account(123456789, 1234, 800, 200));
		fund = new Fund(50, 10);
		fundList = List.of(
				new Fund(50, 10),
				new Fund(20, 30),
				new Fund(10, 30),
				new Fund(5, 20)
		);
	}

	@AfterEach
	public void tearDown() {
		validAccount = null;
		fund = null;
		fundList = null;
	}

	@Test
	@DisplayName("Updates ATM Machine stock")
	void testUpdateStock() {
		// given
		long quantity = 700;

		//  when
		when(fundService.findAll()).thenReturn(fundList);
		List<Fund> fundListAfter = atmMachineService.withdrawFunds(validAccount.get(), quantity);

		//  then
		assertThat(fundListAfter)
				.isNotEmpty()
				.hasSize(2);
		assertThat(fundListAfter)
				.element(0).hasFieldOrPropertyWithValue("bill", 50)
				.hasFieldOrPropertyWithValue("quantity", 10L);
		assertThat(fundListAfter)
				.element(1).hasFieldOrPropertyWithValue("bill", 20)
				.hasFieldOrPropertyWithValue("quantity", 10L);
		verify(fundService, times(1)).findAll();
	}

}

package com.zw.atm.service;

import com.zw.atm.exception.InvalidRequestException;
import com.zw.atm.repo.dao.FundDAO;
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
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class FundServiceUnitTest {

	@Mock
	private FundDAO fundDAO;

	@Autowired
	@InjectMocks
	private FundServiceImpl fundService;

	private Fund fund;
	private List<Fund> fundList;

	@BeforeEach
	public void setUp() {
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
		fund = null;
		fundList = null;
	}

	@Test
	@DisplayName("Updates ATM Machine stock")
	void testUpdateStock() {
		// given
		long quantity = 5;

		//  when
		when(fundDAO.save(any())).thenReturn(fund);
		Fund fundAfter = fundService.updateStock(fund, quantity);

		//  then
		assertThat(fundAfter)
				.isNotNull()
				.hasFieldOrPropertyWithValue("quantity", quantity);
		verify(fundDAO, times(1)).save(fund);
	}

	@Test
	@DisplayName("Gets all the ATM Machine stock")
	void testFindAll() {
		//  when
		when(fundDAO.findAll(any(Sort.class))).thenReturn(fundList);
		List<Fund> funds = fundService.findAll();

		//  then
		assertThat(funds).isEqualTo(fundList);
		verify(fundDAO, times(1)).findAll(any(Sort.class));
	}

	@Test
	@DisplayName("Withdrawal request validation with invalid amount")
	void testIsValidRequestWithInvalidAmount() {
		//  given
		int quantity = 22;

		//  when
		when(fundDAO.findAll()).thenReturn(fundList);

		//  then
		assertThrows(InvalidRequestException.class, () -> {
			fundService.isValidRequest(quantity);
		});
		verify(fundDAO, times(1)).findAll();
	}

	@Test
	@DisplayName("Withdrawal request validation with not enough funds")
	void testIsValidRequestWithNotEnoughFunds() {
		//  given
		int quantity = 9999;

		//  when
		when(fundDAO.findAll()).thenReturn(fundList);

		//  then
		assertThrows(InvalidRequestException.class, () -> {
			fundService.isValidRequest(quantity);
		});
		verify(fundDAO, times(1)).findAll();
	}

}

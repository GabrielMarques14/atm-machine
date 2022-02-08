package com.zw.atm.service;

import com.zw.atm.exception.InvalidRequestException;
import com.zw.atm.repo.dao.AccountDAO;
import com.zw.atm.repo.entity.Account;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class AccountServiceUnitTest {

	@Mock
	private AccountDAO accountDAO;

	@Autowired
	@InjectMocks
	private AccountServiceImpl accountService;

	private Optional<Account> validAccount;
	private Optional<Account> invalidAccount;

	@BeforeEach
	public void setUp() {
		validAccount = Optional.of(new Account(123456789, 1234, 800, 200));
		invalidAccount = Optional.ofNullable(null);
	}

	@AfterEach
	public void tearDown() {
		validAccount = null;
		invalidAccount = null;
	}

	@Test
	@DisplayName("Find an existing account by number")
	void testFindAccountByNumberWithSuccess() {
		//  given
		long number = 123456789;

		//  when
		when(accountDAO.findById(any())).thenReturn(validAccount);
		Optional<Account> account = accountService.findAccountByNumber(number);

		//  then
		assertThat(account)
				.isPresent()
				.get().isEqualTo(validAccount.get());
		verify(accountDAO, times(1)).findById(number);
	}

	@Test
	@DisplayName("Find an nonexistent account by number")
	void testFindAccountByNumberNotFound() {
		//  given
		long number = 123456789;

		//  when
		when(accountDAO.findById(any())).thenReturn(invalidAccount);
		Optional<Account> account = accountService.findAccountByNumber(123456789);

		//  then
		assertThat(account).isNotPresent();
		verify(accountDAO, times(1)).findById(number);
	}

	@Test
	@DisplayName("Update balance after withdrawing a quantity smaller than account balance")
	void testUpdateBalanceWithBalanceSmallerThanQuantity() {
		//  given
		long quantity = 200;

		//  when
		when(accountDAO.findById(any())).thenReturn(validAccount);
		when(accountDAO.save(any())).thenReturn(validAccount.get());
		accountService.updateBalance(validAccount.get(), quantity);

		//  then
		assertThat(validAccount)
				.isPresent().get()
						.hasFieldOrPropertyWithValue("balance", 600L);
		verify(accountDAO, times(1)).save(validAccount.get());
	}

	@Test
	@DisplayName("Update balance after withdrawing a quantity bigger than account balance")
	void testUpdateBalanceWithBalanceBiggerThanQuantity() {
		//  given
		long quantity = 9999;

		//  when
		when(accountDAO.findById(any())).thenReturn(validAccount);
		when(accountDAO.save(any())).thenReturn(validAccount.get());
		accountService.updateBalance(validAccount.get(), quantity);

		//  then
		assertThat(validAccount)
				.isPresent().get()
				.hasFieldOrPropertyWithValue("balance", 800L);
		verify(accountDAO, times(1)).save(validAccount.get());
	}

	@Test
	@DisplayName("Withdrawal request validation with incorrect PIN")
	void testIsValidWithdrawalRequestWithIncorrectPIN() {
		//  given
		long quantity = 200;

		//  when
		when(accountDAO.findById(any())).thenReturn(validAccount);

		//  then
		assertThrows(InvalidRequestException.class, () -> {
			accountService.isValidWithdrawalRequest(
					new Account(123456789, 1111, 800, 200), quantity);
		});
		verify(accountDAO, times(1)).findById(validAccount.get().getNumber());
	}

	@Test
	@DisplayName("Withdrawal request validation with not enough funds")
	void testIsValidWithdrawalRequestWithNotEnoughFunds() {
		//  given
		long quantity = 9999;

		//  when
		when(accountDAO.findById(any())).thenReturn(validAccount);

		//  then
		assertThrows(InvalidRequestException.class, () -> {
			accountService.isValidWithdrawalRequest(validAccount.get(), quantity);
		});
		verify(accountDAO, times(1)).findById(validAccount.get().getNumber());
	}

	@Test
	@DisplayName("Balance check validation with incorrect PIN")
	void testIsValidBalanceCheckWithIncorrectPIN() {
		//  given
		long quantity = 200;

		//  when
		when(accountDAO.findById(any())).thenReturn(validAccount);

		//  then
		assertThrows(InvalidRequestException.class, () -> {
			accountService.isValidBalanceCheck(
					new Account(123456789, 1111, 800, 200));
		});
		verify(accountDAO, times(1)).findById(validAccount.get().getNumber());
	}

}

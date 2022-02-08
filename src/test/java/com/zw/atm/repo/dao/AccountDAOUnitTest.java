package com.zw.atm.repo.dao;

import com.zw.atm.repo.entity.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
@Transactional
class AccountDAOUnitTest {

	@Autowired
	private AccountDAO accountDAO;

	@Test
	@DisplayName("Creates a new account")
	void testAccountCreation() {
		//  given
		Account acc = new Account(999999999, 1111, 1000, 100);

		//  when
		acc = accountDAO.save(acc);

		//  then
		assertThat(acc)
				.isNotNull()
				.hasFieldOrPropertyWithValue("number", 999999999L);
	}

	@Test
	@DisplayName("Find an existing account by number")
	void testFindAccountByNumberWithSuccess() {
		//  given
		long number = 123456789;
		Account newAcc = new Account(number, 1234, 800, 200);

		//  when
		accountDAO.save(newAcc);
		Optional<Account> acc = accountDAO.findById(number);

		//  then
		assertThat(acc)
				.isPresent()
				.get().hasFieldOrPropertyWithValue("number", 123456789L);
	}

	@Test
	@DisplayName("Find an nonexistent account by number")
	void testFindAccountByNumberNotFound() {
		// given
		long number = 1;

		//  when
		Optional<Account> acc = accountDAO.findById(number);

		//  then
		assertThat(acc).isNotPresent();
	}

}

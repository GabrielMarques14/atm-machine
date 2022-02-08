package com.zw.atm.repo.dao;

import com.zw.atm.repo.entity.Fund;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
@Transactional
class FundDAOUnitTest {

	@Autowired
	private FundDAO fundDAO;

	@BeforeEach
	void setUp() {
		fundDAO.saveAll(List.of(
				new Fund(50, 10),
				new Fund(20, 30),
				new Fund(10, 30),
				new Fund(5, 10)
		));
	}

	@AfterEach
	void tearDown() {
		fundDAO.deleteAll();
	}

	@Test
	@DisplayName("Find stock for all bills")
	void testFindAll() {
		//  when
		List<Fund> fundList = fundDAO.findAll();

		//  then
		assertThat(fundList)
				.isNotEmpty();
	}

	@Test
	@DisplayName("Update the stock for a specific bill")
	void testUpdateStock() {
		//  given
		long quantity = 9999;

		//  when
		List<Fund> fundList = fundDAO.findAll();
		Optional<Fund> stockOptional = fundList.stream().findFirst();

		assertThat(stockOptional).isPresent();

		stockOptional.ifPresent(s -> s.setQuantity(quantity));
		Fund fundAfter = fundDAO.save(stockOptional.get());

		//  then
		assertThat(fundAfter)
				.isNotNull()
				.hasFieldOrPropertyWithValue("quantity", quantity);
	}

}

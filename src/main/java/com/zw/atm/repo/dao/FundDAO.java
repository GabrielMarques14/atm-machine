package com.zw.atm.repo.dao;

import com.zw.atm.repo.entity.Fund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FundDAO extends JpaRepository<Fund, Long> {

}

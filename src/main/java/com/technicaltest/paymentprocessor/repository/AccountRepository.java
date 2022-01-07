package com.technicaltest.paymentprocessor.repository;

import com.technicaltest.paymentprocessor.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {

}

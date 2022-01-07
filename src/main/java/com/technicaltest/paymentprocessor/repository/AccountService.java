package com.technicaltest.paymentprocessor.repository;

import com.technicaltest.paymentprocessor.entity.Account;

public interface AccountService {

    public Account getAccountById(int account_id);

    public Account updateAccount(Account account);

}

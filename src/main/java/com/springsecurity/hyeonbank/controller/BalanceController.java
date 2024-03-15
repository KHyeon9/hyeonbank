package com.springsecurity.hyeonbank.controller;

import com.springsecurity.hyeonbank.model.AccountTransactions;
import com.springsecurity.hyeonbank.repository.AccountTransactionsRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BalanceController {

    private AccountTransactionsRepository accountTransactionsRepository;

    public BalanceController(AccountTransactionsRepository accountTransactionsRepository) {
        this.accountTransactionsRepository = accountTransactionsRepository;
    }

    @GetMapping("/myBalance")
    public List<AccountTransactions> getBalanceDetails(@RequestParam int id) {
        List<AccountTransactions> accountTransactions
                = accountTransactionsRepository.findByCustomerIdOrderByTransactionDtDesc(id);

        return accountTransactions;
    }
}

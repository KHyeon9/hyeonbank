package com.springsecurity.hyeonbank.controller;

import com.springsecurity.hyeonbank.model.AccountTransactions;
import com.springsecurity.hyeonbank.model.Customer;
import com.springsecurity.hyeonbank.repository.AccountTransactionsRepository;
import com.springsecurity.hyeonbank.repository.CustomerRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BalanceController {

    private AccountTransactionsRepository accountTransactionsRepository;
    private CustomerRepository customerRepository;

    public BalanceController(AccountTransactionsRepository accountTransactionsRepository, CustomerRepository customerRepository) {
        this.accountTransactionsRepository = accountTransactionsRepository;
        this.customerRepository = customerRepository;
    }

    @GetMapping("/myBalance")
    public List<AccountTransactions> getBalanceDetails(@RequestParam String email) {
        List<Customer> customers = customerRepository.findByEmail(email);

        if (customers != null && !customers.isEmpty()) {
            List<AccountTransactions> accountTransactions = accountTransactionsRepository
                    .findByCustomerIdOrderByTransactionDtDesc(customers.get(0).getId());

            if (accountTransactions != null) {
                return accountTransactions;
            }
        }

        return null;
    }
}

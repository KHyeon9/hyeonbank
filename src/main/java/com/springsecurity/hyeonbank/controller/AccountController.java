package com.springsecurity.hyeonbank.controller;

import com.springsecurity.hyeonbank.model.Accounts;
import com.springsecurity.hyeonbank.model.Customer;
import com.springsecurity.hyeonbank.repository.AccountsRepository;
import com.springsecurity.hyeonbank.repository.CustomerRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AccountController {

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;

    public AccountController(AccountsRepository accountsRepository, CustomerRepository customerRepository) {
        this.accountsRepository = accountsRepository;
        this.customerRepository = customerRepository;
    }

    @GetMapping("/myAccount")
    public Accounts getAccountDetails(@RequestParam String email) {
        List<Customer> customers = customerRepository.findByEmail(email);

        if (customers != null && !customers.isEmpty()) {
            Accounts accounts = accountsRepository.findByCustomerId(customers.get(0).getId());

            if (accounts != null) {
                return accounts;
            }
        }

        return null;
    }
}

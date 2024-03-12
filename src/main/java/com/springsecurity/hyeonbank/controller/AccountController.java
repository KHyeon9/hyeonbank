package com.springsecurity.hyeonbank.controller;

import com.springsecurity.hyeonbank.model.Accounts;
import com.springsecurity.hyeonbank.repository.AccountsRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    private AccountsRepository accountsRepository;

    public AccountController(AccountsRepository accountsRepository) {
        this.accountsRepository = accountsRepository;
    }

    @GetMapping("/myAccount")
    public Accounts getAccountDetails(@RequestParam int customerId) {
        Accounts accounts = accountsRepository.findByCustomerId(customerId);
        return accounts;
    }
}

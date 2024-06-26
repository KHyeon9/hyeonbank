package com.springsecurity.hyeonbank.controller;

import com.springsecurity.hyeonbank.model.Customer;
import com.springsecurity.hyeonbank.model.Loans;
import com.springsecurity.hyeonbank.repository.CustomerRepository;
import com.springsecurity.hyeonbank.repository.LoanRepository;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LoansController {

    private LoanRepository loanRepository;
    private CustomerRepository customerRepository;

    public LoansController(LoanRepository loanRepository, CustomerRepository customerRepository) {
        this.loanRepository = loanRepository;
        this.customerRepository = customerRepository;
    }

    @GetMapping("/myLoans")
    @PostAuthorize("hasRole('USER')")
    public List<Loans> getLoanDetails(@RequestParam String email) {
        List<Customer> customers = customerRepository.findByEmail(email);

        if (customers != null && !customers.isEmpty()) {
            List<Loans> loans = loanRepository.findByCustomerIdOrderByStartDtDesc(customers.get(0).getId());

            if (loans != null) {
                return loans;
            }
        }

        return null;
    }
}

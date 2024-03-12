package com.springsecurity.hyeonbank.controller;

import com.springsecurity.hyeonbank.model.Loans;
import com.springsecurity.hyeonbank.repository.LoanRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LoansController {

    private LoanRepository loanRepository;

    public LoansController(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    @GetMapping("/myLoans")
    public List<Loans> getLoanDetails(@RequestParam int customerId) {
        List<Loans> loans = loanRepository.findByCustomerIdOrderByStartDtDesc(customerId);

        return loans;
    }
}

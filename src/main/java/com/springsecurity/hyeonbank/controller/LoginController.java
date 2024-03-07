package com.springsecurity.hyeonbank.controller;

import com.springsecurity.hyeonbank.model.Customer;
import com.springsecurity.hyeonbank.repository.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    private CustomerRepository customerRepository;

    public LoginController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Customer customer) {
        Customer saveCustomer = null;
        ResponseEntity response = null;

        try {
            saveCustomer = customerRepository.save(customer);

            if (saveCustomer.getId() > 0) {
                response = ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body("유저 정보들이 성공적으로 생성되었습니다.");
            }
        } catch (Exception e) {
            response = ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("예외가 발생되었습니다 : " + e.getMessage());
        }

        return response;
    }
}

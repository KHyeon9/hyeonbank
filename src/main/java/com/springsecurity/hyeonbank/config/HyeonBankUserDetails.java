package com.springsecurity.hyeonbank.config;

import com.springsecurity.hyeonbank.model.Customer;
import com.springsecurity.hyeonbank.repository.CustomerRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HyeonBankUserDetails implements UserDetailsService {

    private CustomerRepository customerRepository;

    public HyeonBankUserDetails(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        String username, password = null;
        List<GrantedAuthority> authories = null;
        List<Customer> customers = customerRepository.findByEmail(email);

        if(customers.isEmpty()) {
            throw  new UsernameNotFoundException("User Details이 User를 찾지 못했습니다. : " + email);
        } else {
            username = customers.get(0).getEmail();
            password = customers.get(0).getPwd();
            authories = new ArrayList<>();
            authories.add(new SimpleGrantedAuthority(customers.get(0).getRole()));
        }
        return new User(username, password, authories);
    }
}

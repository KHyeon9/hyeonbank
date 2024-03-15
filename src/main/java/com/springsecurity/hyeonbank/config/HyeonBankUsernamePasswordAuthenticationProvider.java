package com.springsecurity.hyeonbank.config;

import com.springsecurity.hyeonbank.model.Authority;
import com.springsecurity.hyeonbank.model.Customer;
import com.springsecurity.hyeonbank.repository.CustomerRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class HyeonBankUsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    private CustomerRepository customerRepository;
    private PasswordEncoder passwordEncoder;

    public HyeonBankUsernamePasswordAuthenticationProvider(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String pwd = authentication.getCredentials().toString();
        List<Customer> customers = customerRepository.findByEmail(username);

        if (!customers.isEmpty()) {
            if (passwordEncoder.matches(pwd, customers.get(0).getPwd())) {
//                List<GrantedAuthority> authorities = new ArrayList<>();
//                authorities.add(new SimpleGrantedAuthority(customers.get(0).getRole()));
                return new UsernamePasswordAuthenticationToken(username, pwd, getGrantedAuthority(customers.get(0).getAuthorities()));
            } else {
                throw new BadCredentialsException("비밀번호가 틀립니다!");
            }
        } else {
            throw new BadCredentialsException("유저 정보가 존재하지 않습니다!");
        }

    }

    private List<GrantedAuthority> getGrantedAuthority(Set<Authority> authorities) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        for (Authority authority : authorities) {
            grantedAuthorities.add(new SimpleGrantedAuthority(authority.getName()));
        }

        return grantedAuthorities;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}

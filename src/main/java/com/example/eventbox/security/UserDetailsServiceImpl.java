package com.example.eventbox.security;

import com.example.eventbox.model.entity.Customer;
import com.example.eventbox.model.enums.UserRole;
import com.example.eventbox.service.CustomerService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final CustomerService customerService;
    public UserDetailsServiceImpl(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        Optional<Customer> customerOptional = customerService.findByEmail(username);
        Customer customer = customerOptional.orElseThrow(() -> new UsernameNotFoundException("Username not found" + username));
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(UserRole.ROLE_CUSTOMER.name()));
        return new User(username, customer.getPassword(), authorities);
    }
}

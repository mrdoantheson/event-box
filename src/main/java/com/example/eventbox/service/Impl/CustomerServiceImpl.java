package com.example.eventbox.service.Impl;

import com.example.eventbox.model.entity.Customer;
import com.example.eventbox.repository.CustomerRepository;
import com.example.eventbox.service.CustomerService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        return customerRepository.findByEmailAndDeletedFalse(email);
    }
}

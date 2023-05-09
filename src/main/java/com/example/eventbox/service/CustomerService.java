package com.example.eventbox.service;

import com.example.eventbox.model.entity.Customer;

import java.util.Optional;

public interface CustomerService {
    Optional<Customer> findByEmail(String email);
}

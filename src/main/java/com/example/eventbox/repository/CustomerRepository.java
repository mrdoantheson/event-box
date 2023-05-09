package com.example.eventbox.repository;

import com.example.eventbox.model.entity.Customer;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends BaseRepository<Customer, Long> {
    Optional<Customer> findByEmailAndDeletedFalse(String email);

}

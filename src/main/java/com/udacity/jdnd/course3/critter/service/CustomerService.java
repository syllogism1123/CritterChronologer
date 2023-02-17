package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.user.Customer;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Long save(Customer customer) {
        return customerRepository.save(customer).getId();
    }

    public Customer getCustomerById(Long Id) {
        return customerRepository.getReferenceById(Id);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }


}

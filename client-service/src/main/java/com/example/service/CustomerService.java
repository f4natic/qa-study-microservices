package com.example.service;

import com.example.model.Customer;
import com.example.repository.CustomerRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class CustomerService implements CrudService<Customer> {
    private static final Logger logger = LogManager.getLogger(CustomerService.class);
    private CustomerRepository customerRepository;

    public CustomerService(@Autowired CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Long getTotal() {
        return customerRepository.count();
    }

    @Override
    public Page<Customer> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Customer findById(Long id) {
        return null;
    }

    @Override
    public Customer create(Customer customer) {
        return null;
    }

    @Override
    public Customer update(Long id, Customer customer) {
        return null;
    }

    @Override
    public void delete(String name) {

    }
}

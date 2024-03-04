package com.example.service;

import com.example.model.Customer;
import com.example.repository.CustomerRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

import static com.example.utils.DataValidator.validateEmail;

@Service
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
        return customerRepository.findAll(pageable);
    }

    @Override
    public Customer findById(Long id) {
        Optional<Customer> customerOptional = customerRepository.findById(id);
        if(customerOptional.isEmpty()) {
            throw new CustomerException(String.format("Customer with ID: %s, not foud", id));
        }
        return customerOptional.get();
    }

    @Override
    public Customer create(Customer customer) {
        logger.info(String.format("Create customer: %s", customer));
        if(customer.getFirstName() == null || customer.getLastName() == null || customer.getEmail() == null || customer.getPhoneNumber() == null) {
            throw new CustomerException("Required fields must be filled in.");
        }

        if(!validateEmail(customer.getEmail())) {
            throw new CustomerException("Email does not match template example@example.com");
        }

        return customerRepository.save(customer);
    }

    @Override
    public Customer update(Long id, Customer customer) {
        return null;
    }

    @Override
    public void delete(String name) {

    }
}

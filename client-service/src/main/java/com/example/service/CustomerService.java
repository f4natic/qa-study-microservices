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

import static com.example.utils.DataValidator.*;

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
        logger.info(String.format("Find customer with ID: %s", id));
        Optional<Customer> customerOptional = customerRepository.findById(id);
        if(customerOptional.isEmpty()) {
            throw new CustomerException(String.format("Customer with ID: %s, not found", id));
        }
        return customerOptional.get();
    }

    @Override
    public Customer create(Customer customer) {
        logger.info(String.format("Create customer: %s", customer));
        validateCustomer(customer);
        return customerRepository.save(customer);
    }

    @Override
    public Customer update(Long id, Customer customer) {
        logger.info(String.format("Update customer with ID: %s", id));
        Optional<Customer> customerOptional = customerRepository.findById(id);
        if(customerOptional.isEmpty()) {
            throw new CustomerException(String.format("Customer with ID: %s, not found", id));
        }
        validateCustomer(customer);
        Customer dbCustomer = customerOptional.get();
        dbCustomer.setEmail(customer.getEmail());
        dbCustomer.setFirstName(customer.getFirstName());
        dbCustomer.setLastName(customer.getLastName());
        dbCustomer.setPhoneNumber(customer.getPhoneNumber());
        return customerRepository.save(dbCustomer);
    }

    @Override
    public void delete(Long id) {
        logger.info(String.format("Delete customer with ID: %s", id));
        Optional<Customer> customerOptional = customerRepository.findById(id);
        if(customerOptional.isEmpty()) {
            throw new CustomerException(String.format("Customer with ID: %s, not found", id));
        }
        customerRepository.delete(customerOptional.get());
    }

    private void validateCustomer(Customer customer) {
        if(customer.getFirstName() == null || customer.getLastName() == null || customer.getEmail() == null || customer.getPhoneNumber() == null) {
            throw new CustomerException("Required fields must be filled in");
        }
        if(!validateEmail(customer.getEmail())) {
            throw new CustomerException("Email does not match template example@example.com");
        }
        if(!validateName(customer.getFirstName()) || !validateName(customer.getLastName())) {
            throw new CustomerException("First or last name must be no longer than 20 characters and contain only Latin characters");
        }
        if(!validatePhoneNumber(customer.getPhoneNumber())) {
            throw new CustomerException("The phone number must be in the format +00000000000 and be 12 characters");
        }
    }
}

package com.example.service;

import com.example.model.Customer;
import com.example.repository.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.example.constant.Constant.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;
    @InjectMocks
    private CustomerService customerService;

    private Customer customer;

    private int pageNumber = 0;
    private int pageSize = 100;
    private long totalObject = 100L;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        customer = new Customer.Builder()
                .id(ID).firstName(FIRST_NAME)
                .lastName(LAST_NAME).email(EMAIL)
                .phoneNumber(PHONE_NUMBER).build();
    }

    @Test
    public void shouldReturnTotalObject() {
        when(customerRepository.count()).thenReturn(totalObject);
        long total = customerService.getTotal();
        assertEquals(total, totalObject);
    }

    @Test
    public void shouldReturnPageableList() {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        when(customerRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(customer)));
        Page<Customer> page = customerService.findAll(pageable);
        assertEquals(page.getContent().get(0), customer);
    }

    @Test
    public void shouldReturnCustomerById() {
        when(customerRepository.findById(ID)).thenReturn(Optional.of(customer));
        Customer result = customerService.findById(ID);
        assertEquals(result, customer);
    }

    @Test
    public void shouldReturnCustomerExceptionWhenFindById() {
        when(customerRepository.findById(ID)).thenReturn(Optional.empty());
        Exception exception = assertThrows(CustomerException.class, ()-> {
            customerService.findById(ID);
        });
        String expectedMessage = String.format("Customer with ID: %s, not foud", ID);
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void shouldReturnCustomerExceptionWhenCreateCustomerWithEmptyField() {
        Customer wrongCustomer = new Customer.Builder().build();
        Exception exception = assertThrows(CustomerException.class, ()-> {
            customerService.create(wrongCustomer);
        });
        String expectedMessage = "Required fields must be filled in.";
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void shouldReturnCustomerExceptionWhenCreateCustomerWithWrongEmail() {
        Customer wrongCustomer = new Customer.Builder().id(ID).email("АБВГД@example.com")
                .firstName(FIRST_NAME).lastName(LAST_NAME).phoneNumber(PHONE_NUMBER).build();
        Exception exception = assertThrows(CustomerException.class, ()-> {
            customerService.create(wrongCustomer);
        });
        String expectedMessage = "Email does not match template example@example.com";
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(expectedMessage, actualMessage);
    }
}

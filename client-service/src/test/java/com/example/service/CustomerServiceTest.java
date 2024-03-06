package com.example.service;

import com.example.model.Customer;
import com.example.repository.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
        String expectedMessage = String.format("Customer with ID: %s, not found", ID);
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void shouldReturnCustomerExceptionWhenCreateCustomerWithEmptyField() {
        Customer wrongCustomer = new Customer.Builder().build();
        Exception exception = assertThrows(CustomerException.class, ()-> {
            customerService.create(wrongCustomer);
        });
        String expectedMessage = "Required fields must be filled in";
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

    @ParameterizedTest
    @CsvSource({
            "qazwsxedcrfvtgbyhnujm, TestLastName",
            "TestFirstName, qazwsxedcrfvtgbyhnujm"
    })
    public void shouldReturnCustomerExceptionWhenCreateCustomerWithLongName(String first, String last) {
        Customer wrongCustomer = new Customer.Builder().id(ID).email("example@example.com")
                .firstName(first).lastName(last).phoneNumber(PHONE_NUMBER).build();
        Exception exception = assertThrows(CustomerException.class, ()-> {
            customerService.create(wrongCustomer);
        });
        String expectedMessage = "First or last name must be no longer than 20 characters and contain only Latin characters";
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @ParameterizedTest
    @CsvSource({
            "q1 w2, TestLastName",
            "TestFirstName, q1 w2"
    })
    public void shouldReturnCustomerExceptionWhenCreateCustomerWithWrongCharactersInName(String first, String last) {
        Customer wrongCustomer = new Customer.Builder().id(ID).email("example@example.com")
                .firstName(first).lastName(last).phoneNumber(PHONE_NUMBER).build();
        Exception exception = assertThrows(CustomerException.class, ()-> {
            customerService.create(wrongCustomer);
        });
        String expectedMessage = "First or last name must be no longer than 20 characters and contain only Latin characters";
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void shouldReturnCustomerExceptionWhenCreateCustomerWithWrongPhoneNumber() {
        Customer wrongCustomer = new Customer.Builder().id(ID).email(EMAIL)
                .firstName(FIRST_NAME).lastName(LAST_NAME).phoneNumber("1234567890123").build();
        Exception exception = assertThrows(CustomerException.class, ()-> {
            customerService.create(wrongCustomer);
        });
        String expectedMessage = "The phone number must be in the format +00000000000 and be 12 characters";
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void shouldCreateCustomer() {
        when(customerRepository.save(customer)).thenReturn(customer);
        Customer result = customerService.create(customer);
        assertEquals(result, customer);
    }

    @Test
    public void shouldUpdateCustomer() {
        when(customerRepository.findById(ID)).thenReturn(Optional.of(customer));
        when(customerRepository.save(customer)).thenReturn(customer);
        Customer result = customerService.update(ID, customer);
        assertEquals(result, customer);
    }

    @Test
    public void shouldDeleteCustomer() {
        when(customerRepository.findById(ID)).thenReturn(Optional.of(customer));
        customerService.delete(ID);
    }

    @Test
    public void shouldReturnCustomerExceptionWhenDeleteNonExistingCustomer() {
        when(customerRepository.findById(ID)).thenReturn(Optional.empty());
        Exception exception = assertThrows(CustomerException.class, ()-> {
            customerService.delete(ID);
        });
        String expectedMessage = String.format("Customer with ID: %s, not found", ID);
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(expectedMessage, actualMessage);
    }
}

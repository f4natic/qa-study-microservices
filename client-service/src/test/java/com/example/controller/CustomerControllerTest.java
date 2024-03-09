package com.example.controller;

import com.example.model.Customer;
import com.example.service.CrudService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static com.example.constant.Constant.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private CrudService<Customer> service;

    private int pageNum = 0;
    private int pageSize = 5;
    private long totalCustomers = 10;
    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer.Builder()
                .id(ID)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .phoneNumber(PHONE_NUMBER)
                .build();
    }

    @Test
    public void shouldReturnTotal() throws Exception {
        when(service.getTotal()).thenReturn(totalCustomers);
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/customers/total")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse();
        Long total = objectMapper.readValue(response.getContentAsString(), Long.class);
        Assertions.assertEquals(total, totalCustomers);
    }

    @Test
    public void shouldReturnCustomerList() throws Exception {
        List<Customer> customers = List.of(customer);
        Page<Customer> page = new PageImpl<>(customers, PageRequest.of(pageNum, pageSize), customers.size());
        when(service.findAll(any(Pageable.class))).thenReturn(page);
        mockMvc.perform(MockMvcRequestBuilders.get("/customers")
                        .queryParam("page", String.valueOf(pageNum))
                        .queryParam("pageSize", String.valueOf(pageSize))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.length()").value(customers.size()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(customer.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].firstName").value(customer.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].lastName").value(customer.getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].phoneNumber").value(customer.getPhoneNumber()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].email").value(customer.getEmail()));
    }

    @Test
    public void shouldReturnCustomerById() throws Exception {
        when(service.findById(ID)).thenReturn(customer);
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get(String.format("/customers/%s", ID))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse();
        Customer returnedCustomer = objectMapper.readValue(response.getContentAsString(), Customer.class);
        Assertions.assertEquals(returnedCustomer, customer);
    }
}

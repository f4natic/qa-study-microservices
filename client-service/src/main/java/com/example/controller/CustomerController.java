package com.example.controller;

import com.example.model.Customer;
import com.example.service.CrudService;
import com.example.service.CustomerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Customer Tutorial", description = "Cusomers API for QA")
@RestController
@RequestMapping("/customers")
public class CustomerController {

    private CrudService<Customer> customerService;

    @Autowired
    public CustomerController(CrudService<Customer> customerService) {
        this.customerService = customerService;
    }
    @GetMapping("/total")
    public Long getTotal() {
        return customerService.getTotal();
    }

    @GetMapping(params = {"page", "pageSize"})
    public Page<Customer> findAll(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "5") int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.ASC, "id"));
        return customerService.findAll(pageable);
    }
}

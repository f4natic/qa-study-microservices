package com.example.controller;

import com.example.model.ExceptionResponse;
import com.example.model.Product;
import com.example.service.CrudService;
import com.example.service.ProductException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("products")
public class ProductController {
    private CrudService<Product> productCrudService;

    @Autowired
    public ProductController(CrudService<Product> productCrudService) {
        this.productCrudService = productCrudService;
    }

    @GetMapping
    public Collection<Product> findAll() {
        return productCrudService.findAll();
    }

    @GetMapping("/{name}")
    public Product findByName(@PathVariable String name) {
        return productCrudService.findByName(name);
    }

    @PostMapping("/create")
    public Product create(@RequestBody Product product) {
        return productCrudService.create(product);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ProductException.class)
    @ResponseBody
    ExceptionResponse handleBadRequest(Exception exception) {
        return new ExceptionResponse(exception.getMessage());
    }
}

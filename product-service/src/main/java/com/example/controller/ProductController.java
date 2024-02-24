package com.example.controller;

import com.example.model.ExceptionResponse;
import com.example.model.Product;
import com.example.service.CrudService;
import com.example.service.ProductException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Product Tutorial", description = "Product API for QA")
@RestController
@RequestMapping("/products")
public class ProductController {
    private CrudService<Product> productCrudService;

    @Autowired
    public ProductController(CrudService<Product> productCrudService) {
        this.productCrudService = productCrudService;
    }

    @GetMapping(params = {"page", "pageSize"})
    public Page<Product> findAll(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "5") int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return productCrudService.findAll(pageable);
    }

    @GetMapping("/{name}")
    public Product findByName(@PathVariable String name) {
        return productCrudService.findByName(name);
    }

    @PostMapping("/create")
    public Product create(@RequestBody Product product) {
        return productCrudService.create(product);
    }

    @PutMapping("/{name}")
    public Product update(@PathVariable String name, @RequestBody Product product) {
        return productCrudService.update(name, product);
    }

    @DeleteMapping("/{name}")
    public void delete(@PathVariable String name) {
        productCrudService.delete(name);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ProductException.class)
    @ResponseBody
    ExceptionResponse handleBadRequest(Exception exception) {
        return new ExceptionResponse(exception.getMessage());
    }
}

package com.example.configuration;

import com.example.model.Product;
import com.example.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseConfiguration {

    private ProductService productService;

    public DatabaseConfiguration(@Autowired ProductService productService) {
        this.productService = productService;
    }

    @Bean
    public CommandLineRunner dataInitialization() {
        return args -> {
            // Здесь можно выполнить операции по заполнениюсимволы кириллицы, а так же  таблицы Product
            Product product1 = new Product.Builder()
                    .id(1L)
                    .name("Product-1")
                    .price(59.059)
                    .manufacturer("Manufacturer-First")
                    .build();
            Product product2 = new Product.Builder()
                    .id(2L)
                    .name("Product-2")
                    .price(159.159)
                    .manufacturer("Manufacturer-Second")
                    .build();
            Product product3 = new Product.Builder()
                    .id(3L)
                    .name("Product-3")
                    .price(259.259)
                    .manufacturer("Manufacturer-Third")
                    .build();
            productService.create(product1);
            productService.create(product2);
            productService.create(product3);
        };
    }
}
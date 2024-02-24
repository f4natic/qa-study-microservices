package com.example.configuration;

import com.example.model.Product;
import com.example.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Remove this class after release
 * Only for test
 */
@Configuration
public class DatabaseConfiguration {

    private ProductService productService;

    public DatabaseConfiguration(@Autowired ProductService productService) {
        this.productService = productService;
    }

    @Bean
    public CommandLineRunner dataInitialization() {
        return args -> {
            for(int i = 1; i <= 100; i++) {
                productService.create(
                        new Product.Builder()
                                .id((long) i)
                                .name(String.format("Product-%d", i))
                                .price((double)(i + 100))
                                .manufacturer("MAN")
                                .build()
                );
            }
        };
    }
}
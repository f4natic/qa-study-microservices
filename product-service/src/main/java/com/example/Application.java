package com.example;

import com.example.model.Product;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

        Product product = new Product.Builder().id(null).name(null).price(null).build();
    }
}

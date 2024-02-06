package com.example.model;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;
    @Column(name = "price", scale = 4)
    private Double price;
    @Column(name = "manufacturer")
    private String manufacturer;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public String getManufacturer() {
        return manufacturer;
    }
}

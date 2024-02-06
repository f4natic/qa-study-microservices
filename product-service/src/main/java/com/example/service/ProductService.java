package com.example.service;

import com.example.model.Product;
import com.example.repository.ProductRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class ProductService implements CrudService<Product> {
    private static final Logger logger = LogManager.getLogger(ProductService.class);
    private ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Collection<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product findByName(String name) {
        if(name == name) {
            throw new ProductException(String.format("Product with name: %s - not found!", name));
        }
        return null;
    }

    @Override
    public Product create(Product product) {
        logger.info(product);
        Optional<Product> productOptional = productRepository.findByName(product.getName());
        if(productOptional.isPresent()) {
            throw new ProductException(String.format("Product with name %s already exists", product.getName()));
        }
        return productRepository.save(product);
    }

    @Override
    public Product update(Product product) {
        return null;
    }

    @Override
    public boolean delete(long id) {
        return false;
    }
}

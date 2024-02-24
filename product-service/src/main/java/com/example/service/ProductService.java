package com.example.service;

import com.example.model.Product;
import com.example.repository.ProductRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class ProductService implements CrudService<Product> {
    private static final Logger logger = LogManager.getLogger(ProductService.class);
    private ProductRepository productRepository;

    private Pattern namePattern = Pattern.compile("[\\p{L}\\p{N}\\s-#&]+");
    private Pattern manufacturerPattern = Pattern.compile("[\\p{L}\\s-^*]+");

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Collection<Product> findAll() {
        logger.info("GET ALL PRODUCT");
        return productRepository.findAll();
    }

    @Override
    public Product findByName(String name) {
        Optional<Product> optionalProduct = productRepository.findByName(name);
        if(optionalProduct.isEmpty()) {
            throw new ProductException(String.format("Product with name: %s - not found!", name));
        }
        return optionalProduct.get();
    }

    @Override
    public Product create(Product product) {
        logger.info(String.format("Create product: %s", product));
        if(product.getName() == null || product.getPrice() == null || product.getManufacturer() == null) {
            throw new ProductException(String.format("Required fields must be filled in. %s", product.getName()));
        }
        Optional<Product> productOptional = productRepository.findByName(product.getName());
        if(productOptional.isPresent()) {
            throw new ProductException(String.format("Product with name %s already exists", product.getName()));
        }
        if(!isRequariedName(product.getName())) {
            throw new ProductException("Field name must contain Latin, Cyrillic, Arabic characters and hyphen.");
        }
        if(!isRequariedManufacturer(product.getManufacturer())) {
            throw new ProductException("Field manufacturer must contain Latin, Cyrillic characters and hyphen.");
        }
        return productRepository.save(product);
    }

    @Override
    public Product update(String name, Product product) {
        Optional<Product> productOptional = productRepository.findByName(name);
        if(productOptional.isEmpty()) {
            throw new ProductException(String.format("Product with name %s not found", product.getName()));
        }
        if(product.getName() == null || product.getPrice() == null || product.getManufacturer() == null) {
            throw new ProductException(String.format("Required fields must be filled in. %s", product.getName()));
        }
        if(!isRequariedName(product.getName())) {
            throw new ProductException("Field name must contain Latin, Cyrillic, Arabic characters and hyphen.");
        }
        if(!isRequariedManufacturer(product.getManufacturer())) {
            throw new ProductException("Field manufacturer must contain Latin, Cyrillic characters and hyphen.");
        }
        Product exist = productOptional.get();
        if(!exist.getName().equals(product.getName())) exist.setName(product.getName());
        if(!exist.getPrice().equals(product.getPrice())) exist.setPrice(product.getPrice());
        if(!exist.getManufacturer().equals(product.getManufacturer())) exist.setManufacturer(product.getManufacturer());
        productRepository.delete(exist);
        return productRepository.save(product);
    }

    @Override
    public void delete(String name) {
        Optional<Product> productOptional = productRepository.findByName(name);
        if(productOptional.isEmpty()) {
            throw new ProductException(String.format("Product with name %s not found", name));
        }
        productRepository.delete(productOptional.get());
    }

    private boolean isRequariedName(String name) {
        if (namePattern.matcher(name).matches()) {
            return true;
        }
        return false;
    }

    private boolean isRequariedManufacturer(String manufacturer) {
        if (manufacturerPattern.matcher(manufacturer).matches()) {
            return true;
        }
        return false;
    }
}

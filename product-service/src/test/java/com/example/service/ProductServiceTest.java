package com.example.service;

import com.example.model.Product;
import com.example.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.example.constant.Constant.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProductServiceTest {
    private ProductRepository repository;
    private ProductService service;
    private Product product;

    @BeforeEach
    public void init() {
        repository = mock(ProductRepository.class);
        service = new ProductService(repository);
        product = new Product.Builder()
                .id(ID)
                .name(NAME)
                .price(PRICE)
                .manufacturer(MANUFACTURER)
                .build();
    }

    @Test
    public void shouldReturnProductList() {
        when(repository.findAll()).thenReturn(List.of(product));
        List<Product> products = (List<Product>) service.findAll();
        Assertions.assertEquals(products.get(0), product);
    }

    @Test
    public void shouldReturnProductByName() {
        when(repository.findByName(NAME)).thenReturn(Optional.of(product));
        Product returnedProducts = service.findByName(NAME);
        Assertions.assertEquals(returnedProducts, product);
    }

    @Test
    public void shouldCreateProduct() {
        when(repository.save(product)).thenReturn(product);
        Product createdProduct = service.create(product);
        Assertions.assertEquals(createdProduct, product);
    }

    @Test
    public void shouldReturnErrorWithReqFieldIsNull() {
        Product nullFieldProduct = new Product();
        Exception exception = assertThrows(ProductException.class, () -> {
            service.create(nullFieldProduct);
        });
        String expectedMessage = String.format("Required fields must be filled in. %s", nullFieldProduct.getName());
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void shouldReturnErrorWithProductExist() {
        when(repository.findByName(NAME)).thenReturn(Optional.of(product));
        Exception exception = assertThrows(ProductException.class, () -> {
            service.create(product);
        });
        String expectedMessage = String.format("Product with name %s already exists", product.getName());
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void shouldReturnErrorWithWrongName() {
        product.setName(WRONG_NAME);
        Exception exception = assertThrows(ProductException.class, () -> {
            service.create(product);
        });
        String expectedMessage = "Field name must contain Latin, Cyrillic, Arabic characters and hyphen.";
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void shouldReturnErrorWithWrongManufacturer() {
        product.setManufacturer(WRONG_NAME);
        Exception exception = assertThrows(ProductException.class, () -> {
            service.create(product);
        });
        String expectedMessage = "Field manufacturer must contain Latin, Cyrillic characters and hyphen.";
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(expectedMessage, actualMessage);
    }
}

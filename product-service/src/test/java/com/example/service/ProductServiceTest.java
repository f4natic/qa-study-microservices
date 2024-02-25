package com.example.service;

import com.example.model.Product;
import com.example.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.example.constant.Constant.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ProductServiceTest {
    private ProductRepository repository;
    private ProductService service;
    private Product product;
    private Double editedPrice = 0.0001;

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
        Pageable pageable = Pageable.unpaged();
        when(repository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(product)));
        List<Product> products = (List<Product>) service.findAll(pageable).getContent();
        Assertions.assertEquals(products.get(0), product);
    }

    @Test
    public void shouldReturnProductByName() {
        when(repository.findByName(NAME)).thenReturn(Optional.of(product));
        Product returnedProducts = service.findByName(NAME);
        Assertions.assertEquals(returnedProducts, product);
    }

    @Test
    public void shouldReturnExceptionWhenFindByName() {
        when(repository.findByName(NAME)).thenReturn(Optional.empty());
        Exception exception = assertThrows(ProductException.class, () -> {
            service.findByName(NAME);
        });
        String expectedMessage = String.format("Product with name: %s - not found!", NAME);
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void shouldCreateProduct() {
        when(repository.save(product)).thenReturn(product);
        Product createdProduct = service.create(product);
        Assertions.assertEquals(createdProduct, product);
    }

    @Test
    public void shouldReturnErrorWithReqFieldIsNullWhenCreate() {
        Product nullFieldProduct = new Product();
        Exception exception = assertThrows(ProductException.class, () -> {
            service.create(nullFieldProduct);
        });
        String expectedMessage = String.format("Required fields must be filled in. %s", nullFieldProduct.getName());
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(expectedMessage, actualMessage);
        verify(repository, never()).save(product);
    }

    @Test
    public void shouldReturnErrorWithProductExistWhenCreate() {
        when(repository.findByName(NAME)).thenReturn(Optional.of(product));
        Exception exception = assertThrows(ProductException.class, () -> {
            service.create(product);
        });
        String expectedMessage = String.format("Product with name %s already exists", product.getName());
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(expectedMessage, actualMessage);
        verify(repository, never()).save(product);
    }

    @Test
    public void shouldReturnErrorWithWrongNameWhenCreate() {
        product.setName(WRONG_NAME);
        Exception exception = assertThrows(ProductException.class, () -> {
            service.create(product);
        });
        String expectedMessage = "Field name must contain Latin, Cyrillic, Arabic characters and hyphen.";
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(expectedMessage, actualMessage);
        verify(repository, never()).save(product);
    }

    @Test
    public void shouldReturnErrorWithWrongManufacturerWhenCreate() {
        product.setManufacturer(WRONG_NAME);
        Exception exception = assertThrows(ProductException.class, () -> {
            service.create(product);
        });
        String expectedMessage = "Field manufacturer must contain Latin, Cyrillic characters and hyphen.";
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(expectedMessage, actualMessage);
        verify(repository, never()).save(product);
    }

    @Test
    public void shouldEditProduct() {
        product.setPrice(editedPrice);
        when(repository.findByName(product.getName())).thenReturn(Optional.of(product));
        when(repository.save(product)).thenReturn(product);
        Product editedProduct = service.update(product.getName(), product);
        Assertions.assertEquals(editedProduct, product);
    }

    @Test
    public void shouldReturnErrorWithProductNotExistWhenEdit() {
        when(repository.findByName(product.getName())).thenReturn(Optional.empty());
        Exception exception = assertThrows(ProductException.class, () -> {
            service.update(product.getName(), product);
        });
        String expectedMessage = String.format("Product with name %s not found", product.getName());
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(expectedMessage, actualMessage);
        verify(repository, never()).save(product);
    }

    @Test
    public void shouldReturnErrorWithReqFieldIsNullWhenEdit() {
        product.setPrice(null);
        when(repository.findByName(product.getName())).thenReturn(Optional.of(product));
        when(repository.save(product)).thenReturn(product);
        Exception exception = assertThrows(ProductException.class, () -> {
            service.update(product.getName(), product);
        });
        String expectedMessage = String.format("Required fields must be filled in. %s", product.getName());
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(expectedMessage, actualMessage);
        verify(repository, never()).save(product);
    }

    @Test
    public void shouldReturnErrorWithWrongNameWhenEdit() {
        product.setName(WRONG_NAME);
        when(repository.findByName(NAME)).thenReturn(Optional.of(product));
        Exception exception = assertThrows(ProductException.class, () -> {
            service.update(NAME, product);
        });
        String expectedMessage = "Field name must contain Latin, Cyrillic, Arabic characters and hyphen.";
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(expectedMessage, actualMessage);
        verify(repository, never()).save(product);
    }

    @Test
    public void shouldReturnErrorWithWrongManufacturerWhenEdit() {
        product.setManufacturer(WRONG_NAME);
        when(repository.findByName(NAME)).thenReturn(Optional.of(product));
        Exception exception = assertThrows(ProductException.class, () -> {
            service.update(NAME, product);
        });
        String expectedMessage = "Field manufacturer must contain Latin, Cyrillic characters and hyphen.";
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(expectedMessage, actualMessage);
        verify(repository, never()).save(product);
    }

    @Test
    public void shouldDeteleProduct() {
        when(repository.findByName(NAME)).thenReturn(Optional.of(product));
        assertDoesNotThrow(() -> service.delete(NAME));
        verify(repository, times(1)).delete(product);
    }

    @Test
    public void shouldReturnExceptionWithNotExistWhenDelete() {
        when(repository.findByName(NAME)).thenReturn(Optional.empty());
        Exception exception = assertThrows(ProductException.class, () -> {
            service.delete(NAME);
        });
        String expectedMessage = String.format("Product with name %s not found", NAME);
        String actualMessage = exception.getMessage();
        Assertions.assertEquals(expectedMessage, actualMessage);
        verify(repository, never()).delete(product);
    }
}

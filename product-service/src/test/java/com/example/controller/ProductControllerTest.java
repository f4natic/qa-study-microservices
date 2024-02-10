package com.example.controller;

import com.example.model.Product;
import com.example.service.CrudService;
import com.example.service.ProductException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.example.constant.Constant.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class ProductControllerTest {

    private final String newName = "New Test name";
    private final Double newPrice = 9876.1234;
    private final String newManufacturer = "New Test Manufacturer Name";
    private Product product;
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockBean private CrudService<Product> service;

    @BeforeEach
    public void init() {
        product = new Product.Builder()
                .id(ID)
                .name(NAME)
                .price(PRICE)
                .manufacturer(MANUFACTURER)
                .build();
    }

    @Test
    public void shouldReturnProductList() throws Exception {
        when(service.findAll()).thenReturn(List.of(product));
        MockHttpServletResponse response = mockMvc.perform(get("/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse();
        List<Product> products = objectMapper.readValue(response.getContentAsString(), new TypeReference<List<Product>>() {});
        Assertions.assertEquals(products.get(0), product);
    }

    @Test
    public void shouldReturnProductByName() throws Exception {
        when(service.findByName(NAME)).thenReturn(product);
        MockHttpServletResponse response = mockMvc.perform(get(String.format("/products/%s", NAME))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse();
        Product returnedProduct = objectMapper.readValue(response.getContentAsString(), Product.class);
        Assertions.assertEquals(returnedProduct, product);
    }

    @Test
    public void shouldCreateProduct() throws Exception {
        when(service.create(product)).thenReturn(product);
        MockHttpServletResponse response = mockMvc.perform(post("/products/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk()).andReturn().getResponse();
        Product returnedProduct = objectMapper.readValue(response.getContentAsString(), Product.class);
        Assertions.assertEquals(returnedProduct, product);
    }

    @Test
    public void shouldEditProduct() throws Exception {
        Product editProduct = new Product.Builder()
                .id(ID).name(newName).price(newPrice).manufacturer(newManufacturer).build();

        when(service.update(NAME, editProduct)).thenReturn(editProduct);
        MockHttpServletResponse response = mockMvc.perform(put(String.format("/products/%s", NAME))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(editProduct)))
                .andExpect(status().isOk()).andReturn().getResponse();
        Product returnedProduct = objectMapper.readValue(response.getContentAsString(), Product.class);
        Assertions.assertEquals(returnedProduct, editProduct);
    }

    @Test
    public void shouldDeleteProduct() throws Exception {
        mockMvc.perform(delete(String.format("/products/%s", NAME))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse();
    }

    @Test
    public void shouldReturnExceptionWhenFindByName() throws Exception {
        String errorMessage = String.format("Product with name: %s - not found!", NAME);
        ProductException productException = new ProductException(errorMessage);
        when(service.findByName(NAME)).thenThrow(productException);

        MockHttpServletResponse response = mockMvc.perform(get(String.format("/products/%s", NAME))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn().getResponse();
        ProductException exception = objectMapper.readValue(response.getContentAsString(), ProductException.class);

        assertEquals(errorMessage,exception.getMessage());
    }
}

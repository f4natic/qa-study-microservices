package com.example.controller;

import com.example.model.Product;
import com.example.service.CrudService;
import com.example.service.ProductException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.example.constant.Constant.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    private final String newName = "New Test name";
    private final Double newPrice = 9876.1234;
    private final String newManufacturer = "New Test Manufacturer Name";

    private int pageNum = 0;
    private int pageSize = 5;
    private long totalProduct = 10;

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
    public void shouldReturnTotal() throws Exception {
        when(service.getTotal()).thenReturn(totalProduct);
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/products/total")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse();
        Long total = objectMapper.readValue(response.getContentAsString(), Long.class);
        Assertions.assertEquals(total, totalProduct);
    }

    @Test
    public void shouldReturnProductList() throws Exception {
        List<Product> products = List.of(product);

        Page<Product> page = new PageImpl<>(products, PageRequest.of(pageNum, pageSize), products.size());
        when(service.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/products")
                        .queryParam("page", String.valueOf(pageNum))
                        .queryParam("pageSize", String.valueOf(pageSize))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.length()").value(products.size()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name").value(product.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].price").value(product.getPrice()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].manufacturer").value(product.getManufacturer()));
    }

    @Test
    public void shouldReturnProductByName() throws Exception {
        when(service.findByName(NAME)).thenReturn(product);
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get(String.format("/products/%s", NAME))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse();
        Product returnedProduct = objectMapper.readValue(response.getContentAsString(), Product.class);
        Assertions.assertEquals(returnedProduct, product);
    }

    @Test
    public void shouldCreateProduct() throws Exception {
        when(service.create(product)).thenReturn(product);
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.post("/products/create")
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
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.put(String.format("/products/%s", NAME))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(editProduct)))
                .andExpect(status().isOk()).andReturn().getResponse();
        Product returnedProduct = objectMapper.readValue(response.getContentAsString(), Product.class);
        Assertions.assertEquals(returnedProduct, editProduct);
    }

    @Test
    public void shouldDeleteProduct() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(String.format("/products/%s", NAME))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse();
    }

    @Test
    public void shouldReturnExceptionWhenFindByName() throws Exception {
        String errorMessage = String.format("Product with name: %s - not found!", NAME);
        ProductException productException = new ProductException(errorMessage);
        when(service.findByName(NAME)).thenThrow(productException);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get(String.format("/products/%s", NAME))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn().getResponse();
        ProductException exception = objectMapper.readValue(response.getContentAsString(), ProductException.class);

        assertEquals(errorMessage,exception.getMessage());
    }
}

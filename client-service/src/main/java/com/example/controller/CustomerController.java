package com.example.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Customer Tutorial", description = "Cusomers API for QA")
@RestController
@RequestMapping("/customers")
public class CustomerController {


}

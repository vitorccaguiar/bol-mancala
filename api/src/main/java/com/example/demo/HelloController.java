package com.example.demo;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/hello")
public class HelloController {

	@GetMapping("/test")
    public String getMethodName() {
        return "Test";
    }

}
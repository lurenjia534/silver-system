package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class  HelloWorldController {
    @GetMapping("/hello")
    public String helloWorld() {
        return "Hello World!";
    }

    @GetMapping("/goodbye")
    public String goodbye() {
        return "Goodbye!";
    }

    @GetMapping("/hola")
    public String hola() {
        return "Hola!";
    }

}
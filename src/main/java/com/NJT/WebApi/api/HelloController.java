package com.NJT.WebApi.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hello-controller")
public class HelloController {

    @GetMapping
    public ResponseEntity sayHello() {

        return ResponseEntity.ok("Zdravo!");

    }
}

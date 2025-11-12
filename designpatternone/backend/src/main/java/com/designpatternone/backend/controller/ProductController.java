package com.designpatternone.backend.controller;



import com.designpatternone.backend.repo.InMemoryProductRepo;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "*")
public class ProductController {
    private final InMemoryProductRepo repo = new InMemoryProductRepo();

    @GetMapping
    public Object all(){
        return repo.findAll();
    }
}

package org.developement.asyncprogramming.controllers;

import org.developement.asyncprogramming.model.Inventory;
import org.developement.asyncprogramming.model.Product;
import org.developement.asyncprogramming.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class Controller {

    @Autowired
    public ProductService service;

    @GetMapping(path = "/getInventory")
    public Inventory listProducts() {
        return service.listProducts();
    }

    @GetMapping(path = "/getInventory2")
    public Inventory listProducts2() {
        return service.listProducts2();
    }

    @GetMapping(path = "/getInventory3")
    public Inventory listProducts3() {
        return service.listProducts3();
    }


    @GetMapping(path = "/getProps")
    public List<String> getProps(@RequestBody Map<String,Object> body) {
        return service.getProps(body);
    }

    @GetMapping(path = "/updateProps")
    public void UpdateProps(@RequestBody Map<String,Object> body) {
        service.updateProps(body);
    }
}

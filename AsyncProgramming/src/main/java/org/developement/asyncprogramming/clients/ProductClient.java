package org.developement.asyncprogramming.clients;

import org.developement.asyncprogramming.model.response.Products;
import org.developement.asyncprogramming.model.response.Quantites;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "product-service")
public interface ProductClient {

    @GetMapping("/getProducts")
    Products getProducts();

}
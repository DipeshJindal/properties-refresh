package org.developement.asyncprogramming.service;

import feign.RetryableException;
import org.developement.asyncprogramming.clients.AccountClient;
import org.developement.asyncprogramming.clients.ProductClient;
import org.developement.asyncprogramming.helper.EnvHelper;
import org.developement.asyncprogramming.model.Inventory;
import org.developement.asyncprogramming.model.Items;
import org.developement.asyncprogramming.model.response.Products;
import org.developement.asyncprogramming.model.response.Quantites;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Service
public class ProductService {

    public Logger log = LoggerFactory.getLogger(ProductService.class);
    long start;
    long end;

    @Autowired
    private EnvHelper envHelper;

    @Autowired
    private ProductClient productClient;


    @Autowired
    private AccountClient accountClient;


    public Inventory listProducts() {
        start = System.currentTimeMillis();

        ExecutorService service = getPlatformExecutor();

        Future<Products> productsFuture = service.submit(() -> {
            log.info("Thread - " + Thread.currentThread().getName());
            return productClient.getProducts();
        });

        Future<Quantites> quantitesFuture = service.submit(() -> {
            log.info("Thread - " + Thread.currentThread().getName());
            return accountClient.getQuantites();
        });


        try {
            Products product = productsFuture.get();
            Quantites quantites = quantitesFuture.get();
            end = System.currentTimeMillis();
            log.info("time taken with parallelism = " + (end - start));
            return getInventory(product, quantites);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Inventory listProducts3() {
        start = System.currentTimeMillis();

        ExecutorService service = getVirtualExecutor();

        CompletableFuture<Products> futureProducts = CompletableFuture.supplyAsync(() -> {
            log.info("Thread - " + Thread.currentThread().getName());
            return productClient.getProducts();
        }, service);

        CompletableFuture<Quantites> quantitesFuture = CompletableFuture.supplyAsync(() -> {
            log.info("Thread - " + Thread.currentThread().getName());
            return accountClient.getQuantites();
        }, service);

        try {
            Products product = futureProducts.get();
            Quantites quantites = quantitesFuture.get();
            end = System.currentTimeMillis();
            log.info("time taken with parallelism = " + (end - start));
            return getInventory(product, quantites);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private Inventory getInventory(Products product, Quantites quantites) {
        Map<String, Items> map = new HashMap<>();
        product.getProducts().forEach(p -> map.compute(p.getName(), (key, value) -> {
            if (value == null)
                return Items.builder()
                        .name(p.getName())
                        .price(p.getPrice()).build();
            return value;
        }));

        quantites.getQuantities().forEach(p -> map.compute(p.getName(), (key, value) -> {
            if (value != null) {
                value.setQuantity(p.getQuantity());
                return value;
            } else {
                return Items.builder()
                        .name(p.getName())
                        .quantity(p.getQuantity())
                        .price(0).build();
            }
        }));
        return Inventory.builder()
                .items(map.values().stream().toList())
                .build();
    }

    public Inventory listProducts2() {
        start = System.currentTimeMillis();
        try {
            Products product = productClient.getProducts();
            Quantites quantites = accountClient.getQuantites();
            end = System.currentTimeMillis();
            log.info("Thread - " + Thread.currentThread().getName());
            log.info("time taken no parallelism = " + (end - start));
            return getInventory(product, quantites);
        } catch (RetryableException e) {
            if (isConnectionTimeout(e)) {
                log.error(e.getMessage());
                throw new RuntimeException(e);
            } else if (isReadTimeout(e)) {
                log.error(e.getMessage());
                throw new RuntimeException(e);
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    private boolean isConnectionTimeout(RetryableException ex) {
        return ex.getMessage() != null && ex.getMessage().contains("Read timed out");
    }

    private boolean isReadTimeout(RetryableException ex) {
        return ex.getMessage() != null && ex.getMessage().contains("Connect timed out");
    }

    public ExecutorService getVirtualExecutor() {
        ThreadFactory virtualThreadFactory = Thread.ofVirtual().name("virtual-", 0).factory();
        return Executors.newThreadPerTaskExecutor(virtualThreadFactory);
    }

    public ExecutorService getPlatformExecutor() {

        ThreadFactory platformThreadFactory = Thread.ofPlatform().name("platform-", 0).factory();
        return Executors.newFixedThreadPool(2, platformThreadFactory);
//        return Executors.newThreadPerTaskExecutor(platformThreadFactory);
    }


    public List<String> getProps(Map<String, Object> props) {
        List<String> lst = (List)props.get("arrValues");
        List<String> output = new ArrayList<>();
        lst.forEach((element)->{
            output.add(envHelper.getProperty(element));
        });
        return output;
    }

    public void updateProps(Map<String, Object> props) {
        envHelper.setProperty(props);
    }
}

package org.developement.asyncprogramming;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AsyncProgrammingApplication {

    public static void main(String[] args) {
        SpringApplication.run(AsyncProgrammingApplication.class, args);
    }

}

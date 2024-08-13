package org.developement.asyncprogramming.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product {
    @JsonProperty("name")
    String name;
    @JsonProperty("price")
    long price;
}

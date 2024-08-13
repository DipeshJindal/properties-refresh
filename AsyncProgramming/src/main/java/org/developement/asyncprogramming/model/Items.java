package org.developement.asyncprogramming.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Items {
    String name;
    long price;
    long quantity;
}

package org.developement.asyncprogramming.model.response;

import lombok.Getter;
import lombok.Setter;
import org.developement.asyncprogramming.model.Product;

import java.util.List;

@Getter
@Setter
public class Products {
    List<Product> products;
}

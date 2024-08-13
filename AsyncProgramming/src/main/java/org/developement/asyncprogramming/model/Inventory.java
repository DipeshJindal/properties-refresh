package org.developement.asyncprogramming.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class Inventory {
    List<Items> items;
}

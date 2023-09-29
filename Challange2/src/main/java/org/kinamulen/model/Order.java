package org.kinamulen.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private String name;
    private Integer price;
    private String variant1;
    private String variant2;
    private Integer quantity;
}


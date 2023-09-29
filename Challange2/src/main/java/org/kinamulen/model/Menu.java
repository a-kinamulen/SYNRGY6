package org.kinamulen.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
@AllArgsConstructor
public class Menu {
    private final String name;
    private final int price;
    private Map<Integer,Variant> variant1;
    private Map<Integer,Variant> variant2;
}

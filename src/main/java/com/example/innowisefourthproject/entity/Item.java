package com.example.innowisefourthproject.entity;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Item extends AbstractEntity {
    private String name;
    private String description;
    private BigDecimal price;

    public Item(long id, String name, String description, BigDecimal price) {
        super(id);
        this.name = name;
        this.description = description;
        this.price = price;
    }

}

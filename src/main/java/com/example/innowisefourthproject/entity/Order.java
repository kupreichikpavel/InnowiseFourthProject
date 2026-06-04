package com.example.innowisefourthproject.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Order extends AbstractEntity {
    private long userId;
    private long itemId;
    private String itemName;
    private BigDecimal itemPrice;
    private OrderStatus status;
    private LocalDateTime createdAt;

    public Order(long id,
                 long userId,
                 long itemId,
                 String itemName,
                 BigDecimal itemPrice,
                 OrderStatus status,
                 LocalDateTime createdAt) {
        super(id);
        this.userId = userId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Order(long userId, long itemId, OrderStatus status) {
        this.userId = userId;
        this.itemId = itemId;
        this.status = status;
    }
}

package com.example.innowisefourthproject.entity;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
public abstract class AbstractEntity {
    private long id;

    protected AbstractEntity(long id) {
        this.id = id;
    }
}


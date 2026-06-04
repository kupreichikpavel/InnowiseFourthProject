package com.example.innowisefourthproject.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class User extends AbstractEntity {
    private String login;
    private String passwordHash;
    private String name;
    private Role role;

    public boolean isAdmin() {
        return role == Role.ADMIN;
    }

    public User(long id, String login, String passwordHash, String name, Role role) {
        super(id);
        this.login = login;
        this.passwordHash = passwordHash;
        this.name = name;
        this.role = role;
    }
}

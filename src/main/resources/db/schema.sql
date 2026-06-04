DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS items;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       login VARCHAR(64) UNIQUE NOT NULL,
                       password_hash VARCHAR(64) NOT NULL,
                       name VARCHAR(100) NOT NULL,
                       role VARCHAR(20) NOT NULL DEFAULT 'USER',
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       CONSTRAINT users_role_check CHECK (role IN ('USER', 'ADMIN'))
);

CREATE TABLE items (
                       id BIGSERIAL PRIMARY KEY,
                       name VARCHAR(150) NOT NULL,
                       description TEXT,
                       price NUMERIC(10, 2) NOT NULL CHECK (price >= 0),
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE orders (
                        id BIGSERIAL PRIMARY KEY,
                        user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                        item_id BIGINT NOT NULL REFERENCES items(id) ON DELETE RESTRICT,
                        status VARCHAR(20) NOT NULL DEFAULT 'CREATED',
                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        CONSTRAINT orders_status_check CHECK (status IN ('CREATED', 'CANCELLED','COMPLETED'))
);

CREATE INDEX idx_orders_user_id ON orders(user_id);
CREATE INDEX idx_orders_item_id ON orders(item_id);
CREATE INDEX idx_items_name ON items(name);

INSERT INTO users(login, password_hash, name, role)
VALUES
    ('admin', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918', 'Администратор', 'ADMIN'),
    ('user', '04f8996da763b7a969b1028ee3007569eaf3a635486ddab211d512c85b9df8fb', 'Павел', 'USER');

INSERT INTO items(name, description, price)
VALUES
    ('Ноутбук Lenovo', 'Учебный ноутбук для Java-разработки', 1200.00),
    ('Книга Java Core', 'Справочник по Java Core и Collections', 45.50),
    ('Курс Servlet/JSP', 'Практический курс по web-проектам на Java', 150.00),
    ('Мышь Logitech', 'Беспроводная мышь', 35.00);
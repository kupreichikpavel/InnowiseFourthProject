# InnowiseFourthProject

Java Web application based on Servlets, JSP, JDBC and PostgreSQL.

Проект представляет собой учебное web-приложение интернет-магазина. Пользователь может зарегистрироваться, войти в систему, просматривать товары и создавать заказы. Администратор может управлять товарами и обрабатывать заказы.

## Technologies

* Java 17
* Jakarta Servlet API
* JSP
* JSTL
* JDBC
* PostgreSQL
* Maven
* Apache Tomcat 10
* Log4J2
* Lombok
* JUnit 5
* Mockito

## Architecture

Проект реализован с использованием Layered Architecture и MVC.

Основная цепочка обработки запроса:

```text
Browser
↓
Controller
↓
Command
↓
Service
↓
DAO
↓
PostgreSQL
```

### Main layers

```text
controller    — главный Servlet-контроллер
command       — обработчики пользовательских действий
service       — бизнес-логика приложения
dao           — интерфейсы для работы с БД
dao.impl      — JDBC-реализации DAO
entity        — сущности предметной области
filter        — Servlet filters
pool          — connection pool
util          — вспомогательные классы
api           — web-service endpoints
```

## Main functionality

### User

* Registration
* Login
* Logout
* View products
* Create order
* View own orders
* Cancel own orders

### Admin

* Login
* Logout
* View products
* Add products
* Edit products
* Delete products
* View all orders
* Cancel orders
* Complete orders

## Roles

The application supports two roles:

```text
USER
ADMIN
```

A new registered user receives the `USER` role by default.

An administrator can be created through the database schema or by manually updating the user role in the database.

Example:

```sql
UPDATE users
SET role = 'ADMIN'
WHERE login = 'admin';
```

## Database

The project uses PostgreSQL.

Main tables:

```text
users
items
orders
```

### Database configuration

Database connection properties are stored in:

```text
src/main/resources/db.properties
```

Recommended local configuration example:

```properties
db.driver=org.postgresql.Driver
db.url=jdbc:postgresql://localhost:5432/postgres
db.user=YOUR_DATABASE_USER
db.password=YOUR_DATABASE_PASSWORD
db.poolSize=10
```

For security reasons, real database credentials should not be committed to GitHub.

Recommended approach:

```text
db.properties.example — committed to GitHub
db.properties         — local file ignored by Git
```

## Database schema

The schema script should create the following tables:

```text
users
items
orders
```

The `orders` table supports statuses:

```text
CREATED
CANCELLED
COMPLETED
```

Example test users:

```text
login: admin
password: admin
role: ADMIN
```

```text
login: user
password: user
role: USER
```

## How to run

### 1. Clone the repository

```bash
git clone https://github.com/kupreichikpavel/InnowiseFourthProject.git
```

### 2. Open the project

Open the project in IntelliJ IDEA.

### 3. Configure PostgreSQL

Create or check your PostgreSQL database.

Update local database settings in:

```text
src/main/resources/db.properties
```

### 4. Run database schema

Execute the SQL schema script in PostgreSQL.

### 5. Build the project

```bash
mvn clean package
```

### 6. Configure Tomcat

Use Apache Tomcat 10.

Deploy the project as:

```text
InnowiseFourthProject:war exploded
```

### 7. Open the application

```text
http://localhost:8080/InnowiseFourthProject_war_exploded/
```

## Main pages

```text
/index.jsp                  — login page
/pages/register.jsp         — registration page
/pages/main.jsp             — main page
/pages/items.jsp            — product list
/pages/add_item.jsp         — add product page
/pages/edit_item.jsp        — edit product page
/pages/orders.jsp           — orders page
/pages/error_500.jsp        — error page
```

## Main controller

All main UI actions are processed through:

```text
/controller
```

Examples:

```text
/controller?command=show_items
/controller?command=show_orders
```

## Main commands

```text
login
logout
add_user

show_items
open_add_item_page
add_item
open_edit_item_page
update_item
delete_item

create_order
show_orders
cancel_order
complete_order
```

## Web-service

The project contains a simple API endpoint:

```text
GET /api/items
```

It returns a JSON list of products.

Example response:

```json
[
  {
    "id": 1,
    "name": "Ноутбук Lenovo",
    "description": "Учебный ноутбук для Java-разработки",
    "price": 1200.00
  }
]
```

The endpoint is implemented in:

```text
com.example.innowisefourthproject.api.ItemsApiServlet
```

## Security

The project includes:

* Password hashing
* Session-based authentication
* Role-based access control
* SQL injection protection through `PreparedStatement`
* XSS protection through JSTL `<c:out>`
* Security headers filter
* Protected JSP pages through `SecurityFilter`
* UTF-8 request/response encoding through `EncodingFilter`
* Post/Redirect/Get approach for POST operations

## Filters

Implemented filters:

```text
EncodingFilter
SecurityFilter
SecurityHeadersFilter
```

### EncodingFilter

Sets UTF-8 encoding for requests and responses.

### SecurityFilter

Protects pages inside:

```text
/pages/*
```

Unauthorized users are redirected to:

```text
/index.jsp
```

### SecurityHeadersFilter

Adds security headers:

```text
X-Content-Type-Options
X-Frame-Options
Referrer-Policy
Content-Security-Policy
X-XSS-Protection
```

## Connection pool

The project uses a custom connection pool.

Connection settings are loaded from:

```text
src/main/resources/db.properties
```

The pool creates a fixed number of connections and reuses them during application execution.

## Logging

Logging is implemented with Log4J2.

The application logs:

* Controller command execution
* DAO operations
* Service operations
* Connection pool initialization
* Exceptional situations

## Tests

The project uses JUnit 5 and Mockito.

Test classes are located in:

```text
src/test/java
```

Implemented service tests:

```text
ItemServiceImplTest
UserServiceImplTest
OrderServiceImplTest
```

Run tests:

```bash
mvn clean test
```

## Project structure

```text
InnowiseFourthProject
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com.example.innowisefourthproject
│   │   │       ├── api
│   │   │       ├── command
│   │   │       ├── controller
│   │   │       ├── dao
│   │   │       ├── entity
│   │   │       ├── exception
│   │   │       ├── filter
│   │   │       ├── listner
│   │   │       ├── pool
│   │   │       ├── router
│   │   │       ├── service
│   │   │       └── util
│   │   ├── resources
│   │   └── webapp
│   │       ├── index.jsp
│   │       └── pages
│   └── test
│       └── java
│           └── com.example.innowisefourthproject
├── pom.xml
├── README.md
└── .gitignore
```

## Design patterns used

The project uses several design patterns:

```text
MVC
Layered Architecture
DAO
Command
Singleton
Front Controller
```

### MVC

JSP pages are used as View, `Controller` and `Command` classes process requests, and Service/DAO layers work with business logic and database access.

### DAO

DAO classes hide SQL and JDBC details from the service layer.

### Command

Each user action is represented as a separate command class.

Examples:

```text
LoginCommand
ShowItemsCommand
CreateOrderCommand
CancelOrderCommand
```

### Singleton

Several service and DAO implementations use singleton-style access through `getInstance()`.

## Useful SQL checks

Show users:

```sql
SELECT id, login, name, role
FROM users;
```

Show items:

```sql
SELECT id, name, description, price
FROM items;
```

Show orders:

```sql
SELECT o.id,
       o.user_id,
       o.item_id,
       i.name AS item_name,
       i.price AS item_price,
       o.status,
       o.created_at
FROM orders o
JOIN items i ON i.id = o.item_id
ORDER BY o.created_at DESC;
```

## Current status

Implemented:

* User registration
* User login
* User logout
* Role separation: USER / ADMIN
* Product CRUD
* Order creation
* Order cancellation
* Order completion by admin
* Web-service `/api/items`
* Filters
* Logging
* PostgreSQL database access
* Custom connection pool
* JUnit and Mockito tests
* Maven build
* GitHub repository

## Author

Pavel Kupreichik

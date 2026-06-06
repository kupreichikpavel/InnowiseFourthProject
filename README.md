# InnowiseFourthProject

Java Web application based on Servlets, JSP, JDBC and PostgreSQL.

Проект представляет собой простое web-приложение интернет-магазина, где пользователь может зарегистрироваться, войти в систему, просматривать товары и создавать заказы. Администратор может управлять товарами и обрабатывать заказы.

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

## Architecture

Проект реализован с использованием layered architecture и MVC.

Основные слои приложения:

```text
Controller
↓
Command
↓
Service
↓
DAO
↓
Database
```

### Main packages

```text
com.example.innowisefourthproject
├── command       # Command pattern classes
├── controller    # Main controller servlet
├── dao           # DAO interfaces
├── dao.impl      # JDBC DAO implementations
├── entity        # Entity classes
├── exception     # Custom exceptions
├── filter        # Servlet filters
├── listener      # Servlet/session listeners
├── pool          # Custom connection pool
├── service       # Service interfaces
├── service.impl  # Business logic
└── util          # Utility classes
```

## Features

### User

* Registration
* Login
* Logout
* View products
* Create orders
* View own orders
* Cancel own orders

### Admin

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

Regular users are created with the `USER` role by default.

Admin users can be created manually in the database or inserted through `schema.sql`.

## Database

The project uses PostgreSQL.

Recommended database tables:

```text
users
items
orders
```

### Database configuration

Database connection settings are located in:

```text
src/main/resources/db.properties
```

Example:

```properties
db.driver=org.postgresql.Driver
db.url=jdbc:postgresql://localhost:5432/postgres
db.user=postgres
db.password=qwerty
db.poolSize=10
```

Before running the project, make sure PostgreSQL is running and the database exists.

### Database schema

The schema file creates tables and inserts test data.

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

Be careful: the schema script may contain `DROP TABLE`, so running it will delete existing data.

## How to run

1. Clone the repository:

```bash
git clone https://github.com/kupreichikpavel/InnowiseFourthProject.git
```

2. Open the project in IntelliJ IDEA.

3. Configure PostgreSQL connection in:

```text
src/main/resources/db.properties
```

4. Run the database schema script.

5. Build the project with Maven:

```bash
mvn clean package
```

6. Configure Apache Tomcat 10.

7. Deploy the project as WAR exploded.

8. Open the application in browser:

```text
http://localhost:8080/InnowiseFourthProject_war_exploded/
```

## Main pages

```text
/index.jsp                  # Login page
/pages/register.jsp         # Registration page
/pages/main.jsp             # Main page after login
/pages/items.jsp            # Product list
/pages/add_item.jsp         # Add product page
/pages/edit_item.jsp        # Edit product page
/pages/orders.jsp           # Orders page
/pages/error_500.jsp        # Server error page
```

## Main commands

All requests are processed through the main controller:

```text
/controller
```

Examples:

```text
/controller?command=show_items
/controller?command=show_orders
```

Supported commands include:

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

## Security

The project includes:

* Password hashing
* Role-based access logic
* SQL injection protection through `PreparedStatement`
* Session-based authentication
* UTF-8 encoding filter
* Security filter for protected pages
* Security headers filter
* JSTL usage instead of JSP scriptlets

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

Protects `/pages/*` from unauthorized access.

### SecurityHeadersFilter

Adds basic security headers such as:

```text
X-Content-Type-Options
X-Frame-Options
Referrer-Policy
Content-Security-Policy
```

## Connection Pool

The project uses a custom thread-safe connection pool.

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
* Errors and exceptional situations

## Current project status

Implemented:

* Layered architecture
* MVC
* Command pattern
* DAO pattern
* Singleton usage
* JDBC database access
* PostgreSQL schema
* User registration
* User authorization
* User logout
* Product CRUD
* Order creation
* Order cancellation
* Order completion by admin
* Filters
* Logging
* JSTL pages

## TODO

Recommended improvements:

* Add Post/Redirect/Get pattern to prevent repeated POST actions after pressing F5
* Add Web-service endpoint, for example `/api/items`
* Add JUnit and Mockito tests
* Add more Javadoc comments
* Improve CSS styling
* Move real database password out of GitHub
* Replace all unsafe JSP output with `<c:out>`

## Author

Pavel Kupreichik

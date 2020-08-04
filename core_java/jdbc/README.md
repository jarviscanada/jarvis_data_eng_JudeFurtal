# Introduction

The purpose of this application is to help junior developers to get familiar with using Java Database Connectivity (JDBC). The app will use JDBC to execute Create Read Update and Delete (CRUD) operations on a Relational Database Management System (RDBMS) such as PostgresSQL. When developing this application, I learned how to use JDBC to connect to a PostgresSQL instance running in a Docker container. Furthermore, I learned how to interact with the data in the database using a Data Access Object (DAO) design pattern.

# ER Diagram

<p align="center">
<img src="https://github.com/jarviscanada/jarvis_data_eng_JudeFurtal/blob/develop/core_java/jdbc/ER_diagram.png">
</p>

The above Entity Relationship (ER) diagram describes the relationship between the tables in the database. In the center is the `orders` table which has a one-to-one join connection with the `customer` and `salesperson` tables. For every order there is a `order_item` table and each order item will have a single product record found in the `product` table.

# Design Patterns

The Data Access Object (DAO) design pattern is one of the most commonly used design patterns when working with databases using JDBC. DAO provides a solid abstraction layer between the raw JDBC code and the code specific to business logic. When using DAO, it is necessary to have a Data Transfer Object (DTO), which provides a single data domain (ex. tables and subtables). The output and input for a single DAO is a single DTO. However, DAO's can support multiple tables. DAO's provide an advantage when the application is vertically scalable.
 
The other popular design pattern when working with databases using JDBC is the Repository design pattern. In contrast to the DAO design pattern, the Repository design pattern is used only to access a single table. Furthermore, when joining tables, the Repository design pattern joins the tables within the code as opposed to DAO where joins are conducted in the database.  The Repository design pattern has advantages when distributing data globally, when conducting mostly just CRUD operations and if the application is horizontally scalable.


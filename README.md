# Railway Reservation System 🚆

A simple Java-based railway reservation system using JDBC and MySQL.

## 📁 Project Structure

- `src/` – Java source files
- `lib/` – MySQL JDBC driver
- `bin/` – Compiled `.class` files

## 💻 Technologies Used

- Java
- JDBC
- MySQL

## 🛠 How to Run

1. Compile:
   ```bash
   javac -d bin -cp "lib/*" src/*.java

## SQL Table Structure
-- Create Users Table
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role ENUM('admin', 'user') DEFAULT 'user'
);

-- Create Trains Table
CREATE TABLE trains (
    id INT AUTO_INCREMENT PRIMARY KEY,
    train_name VARCHAR(100) NOT NULL,
    from_station VARCHAR(100) NOT NULL,
    to_station VARCHAR(100) NOT NULL,
    total_seats INT NOT NULL,
    available_seats INT NOT NULL
);

-- Create Tickets Table
CREATE TABLE tickets (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    train_id INT NOT NULL,
    passenger_name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (train_id) REFERENCES trains(id)
);


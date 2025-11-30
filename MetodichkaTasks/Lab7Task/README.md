# Secure Spaceport (Lab 7)

## Overview
**Secure Spaceport** is a client-server Java application designed to manage a spaceport's docking system. It demonstrates advanced Java concepts including **Non-blocking I/O (NIO)**, **Multithreading**, **Database Persistence (PostgreSQL)**, and **User Authentication**.

This project is the evolution of previous labs, moving from simple file-based storage to a robust, secure, and concurrent system.

## Key Features
-   **Client-Server Architecture**: Built using `java.nio` (Selector, SocketChannel, ServerSocketChannel) for high-performance non-blocking communication.
-   **Multithreading**: The server uses a `CachedThreadPool` to handle multiple client requests concurrently without blocking the main selector loop.
-   **Database Integration**: All data (Users and Spaceships) is persisted in a **PostgreSQL** database.
-   **Hybrid Storage**: Uses a "Write-Through" cache strategy where data is stored in the DB and cached in a `ConcurrentHashMap` for fast read access.
-   **Security & Authentication**:
    -   User Registration and Login.
    -   **SHA-256** Password Hashing.
    -   **Session Management**: Commands are restricted based on login state.
    -   **Ownership Access Control**: Users can only undock ships they own.
-   **Configuration**: Database credentials are secured in a `db.properties` file (or environment variables).

## Prerequisites
1.  **Java Development Kit (JDK) 21** or higher.
2.  **PostgreSQL Database** installed and running.
3.  **PostgreSQL JDBC Driver** (e.g., `postgresql-42.7.4.jar`).

## Installation & Setup

### 1. Database Setup
1.  Open **pgAdmin** or your preferred SQL tool.
2.  Create a new database named `spaceport`.
    ```sql
    CREATE DATABASE spaceport;
    ```
    *(The server will automatically create the required tables `users` and `spaceships` on the first run.)*

### 2. Project Configuration
1.  Navigate to the project root directory.
2.  Create a `lib` folder and place your PostgreSQL JDBC driver jar file inside it.
    ```
    project_root/
    ├── lib/
    │   └── postgresql-42.7.4.jar
    ├── src/
    ├── db.properties
    ├── run_server.bat
    └── run_client.bat
    ```
3.  Configure database credentials in `db.properties`:
    ```properties
    db.url=jdbc:postgresql://localhost:5432/spaceport
    db.user=postgres
    db.password=your_password
    ```

## Running the Application

### Start the Server
Double-click `run_server.bat` or run via command line:
```cmd
run_server.bat
```
*You should see: "Server started on port 5000" and "Database initialized successfully."*

### Start the Client
Double-click `run_client.bat` or run via command line:
```cmd
run_client.bat
```
*You should see: "Connected to Spaceport Server."*

## Usage Guide

The system enforces authentication. You must log in to perform operations like docking or listing ships.

### 1. Registration & Login
New users must register first.
```
> register [username] [password]
Example: register hamza 123
```
Then log in to start a session.
```
> login [username] [password]
Example: login hamza 123
```

### 2. Docking a Ship
Dock a new ship. It will be assigned to your user ID.
```
> dock [id] [name] [tonnage]
Example: dock 101 X-Wing 50
```

### 3. Listing Ships
View all ships currently docked in the spaceport.
```
> list
```

### 4. Undocking a Ship
You can only undock ships that **you** own.
```
> undock [id]
Example: undock 101
```

### 5. Logout & Exit
End your session or close the client.
```
> logout
> exit
```

## Command Reference

| Command | Usage | Description | Auth Required |
| :--- | :--- | :--- | :--- |
| `register` | `register [user] [pass]` | Create a new account. | No |
| `login` | `login [user] [pass]` | Authenticate and start session. | No |
| `logout` | `logout` | End current session. | Yes |
| `dock` | `dock [id] [name] [tonnage]` | Add a ship to the spaceport. | Yes |
| `undock` | `undock [id]` | Remove a ship (must be owner). | Yes |
| `list` | `list` | Show all docked ships. | Yes |
| `sort_heavy`| `sort_heavy` | List ships sorted by tonnage. | Yes |
| `clear` | `clear` | Remove ALL ships (Admin). | Yes |
| `help` | `help` | Show available commands. | No |
| `exit` | `exit` | Close the client connection. | No |

## Project Structure
-   `com.spaceport.client`: Client-side logic (`Client.java`).
-   `com.spaceport.server`: Server core (`Server.java`), thread pool, session management.
-   `com.spaceport.server.db`: Database interactions (`DatabaseManager`, `UserDAO`, `SpaceportDAO`).
-   `com.spaceport.command`: Command pattern implementation (all command classes).
-   `com.spaceport.common`: Shared models (`User`, `Spaceship`, `Request`, `Response`).

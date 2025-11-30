# Spaceport GUI (Lab 8)

## Overview
**Spaceport GUI** is the latest evolution of the Spaceport project, transforming the console-based application into a fully functional **Graphical User Interface (GUI)** using Java Swing. This version introduces advanced visualization, internationalization, and a user-friendly dashboard while maintaining the robust client-server architecture, database persistence, and security features of previous labs.

## Key Features
-   **Graphical User Interface (GUI)**:
    -   **Swing Framework**: Built using standard Java Swing components (`JFrame`, `JPanel`, `JTable`, `JToolBar`).
    -   **Dashboard**: A centralized main window with a toolbar, ship table, and visualization panel.
    -   **Login Screen**: Dedicated window for user authentication and registration.
-   **Holo-Map Visualization**:
    -   **2D Coordinate System**: Visualizes ships on a grid based on their X and Y coordinates.
    -   **Ownership Color Coding**: Ships owned by the logged-in user are **Green**, others are **Red**.
    -   **Dynamic Scaling**: The map automatically scales to fit the coordinate limits (+/- 1000).
    -   **Animations**: Ships appear with a growing animation effect.
-   **Internationalization (I18n)**:
    -   **Multi-language Support**: Fully localized in **English** and **Turkish**.
    -   **Resource Bundles**: Uses `.properties` files for easy translation management.
    -   **Runtime Switching**: Language can be selected at the login screen.
-   **Enhanced Data Model**:
    -   **Coordinates**: `Spaceship` model updated to include `x` and `y` coordinates.
    -   **Database Updates**: `spaceships` table schema modified to persist coordinate data.
-   **Core Architecture**:
    -   **Client-Server**: Non-blocking I/O (NIO) communication.
    -   **Multithreading**: `SwingWorker` for background network tasks to keep the UI responsive.
    -   **PostgreSQL**: Reliable data persistence.

## Prerequisites
1.  **Java Development Kit (JDK) 21** or higher.
2.  **PostgreSQL Database** installed and running.
3.  **PostgreSQL JDBC Driver** (e.g., `postgresql-42.7.4.jar`) in the `lib` folder.

## Installation & Setup

### 1. Database Setup
1.  Open your PostgreSQL tool (e.g., pgAdmin).
2.  Create a database named `spaceport`.
    ```sql
    CREATE DATABASE spaceport;
    ```
    *(The server automatically handles table creation and schema updates.)*

### 2. Project Configuration
1.  Ensure the `lib` folder contains the PostgreSQL JDBC driver.
    ```
    project_root/
    ├── lib/
    │   └── postgresql-42.7.4.jar
    ```
2.  Configure `db.properties` with your database credentials:
    ```properties
    db.url=jdbc:postgresql://localhost:5432/spaceport
    db.user=postgres
    db.password=your_password
    ```

## Running the Application

### 1. Start the Server
Run the server batch script:
```cmd
run_server.bat
```
*Wait for "Server started on port 5000".*

### 2. Start the GUI Client
Run the GUI client batch script:
```cmd
run_gui.bat
```
*The Login window should appear.*

## Usage Guide

### Login & Registration
-   **Language**: Select "English" or "Türkçe" from the dropdown.
-   **Register**: Enter a username and password, then click "Register" to create an account.
-   **Login**: Enter credentials and click "Login" to access the dashboard.

### Dashboard Features
The main dashboard is split into two sections:
1.  **Ship Table (Left)**: Lists all docked ships with their details (ID, Name, Tonnage, Owner, X, Y).
2.  **Holo-Map (Right)**: A visual representation of the spaceport.
    -   **Green Circles**: Your ships.
    -   **Red Circles**: Other users' ships.
    -   **Size**: Larger circles represent heavier ships.

### Toolbar Commands
-   **Add Ship (Dock)**: Opens a dialog to add a new ship.
    -   **Limit**: X and Y coordinates must be between **-1000** and **+1000**.
-   **Remove Ship (Undock)**: Remove a ship by ID (only if you own it).
-   **Refresh**: Reloads the latest data from the server.
-   **Sort**: Sorts the ship list by tonnage (descending).
-   **Clear**: Removes **ALL** ships from the spaceport (Use with caution!).
-   **Help**: Displays a list of available commands and their usage.
-   **Logout**: Returns to the login screen.
-   **Exit**: Closes the application.

### Filtering
-   Use the **Filter** text field in the toolbar to search for ships by name or type in real-time.

## Project Structure
-   `com.spaceport.client.gui`: Swing components (`MainFrame`, `LoginFrame`, `HoloMapPanel`, `ShipTableModel`).
-   `com.spaceport.client.controller`: Logic bridging the GUI and Network layer (`ClientController`).
-   `com.spaceport.client.util`: Utilities like `ResourceManager` for I18n.
-   `com.spaceport.server`: Server core, connection handling, and thread management.
-   `com.spaceport.server.db`: Database Access Objects (DAOs) and connection pooling.
-   `com.spaceport.command`: Implementation of the Command pattern for all operations.
-   `com.spaceport.common`: Shared data models and network transfer objects.
-   `resources`: Localization files (`messages_en.properties`, `messages_tr.properties`).

## Author
**Hamza Yüksel**
ITMO University - Lab 8 Task

# 🌌 Deep Space 99: Spaceport Docking Manager (Lab 5)

## 🚀 Mission Overview
Welcome, Chief Traffic Controller! You have been assigned to **Deep Space 99**, the busiest spaceport in the sector. Your mission is to manage the influx of interstellar traffic, ensuring that every spaceship is docked safely, categorized correctly, and prioritized based on its tonnage.

This project is the **first phase** of a semester-long simulation where you will build a full-stack Java application from scratch.

## 🎯 Objectives
In this module (Lab 5), we focus on the **foundations** of Java application development:
*   **Object-Oriented Design**: Modeling complex entities like `Spaceship` with encapsulation and validation.
*   **Collections Framework**: Using `HashSet` for unique IDs, `ArrayList` for storage, and `HashMap` for command management.
*   **Command Design Pattern**: Creating a modular and extensible command system.
*   **File Persistence**: Saving and loading station data to ensure no ship is lost when the system reboots.

## 🛠 Features & Commands
The system operates via a robust command-line interface (CLI).

| Command | Usage | Description |
| :--- | :--- | :--- |
| `dock` | `dock [id] [name] [tonnage]` | Registers a new ship to the docking bay. |
| `undock` | `undock [id]` | Clears a ship from the dock and logs its departure. |
| `list` | `list` | Displays the current manifest of all docked ships. |
| `sort_heavy` | `sort_heavy` | Reorders the manifest by tonnage (heaviest first). |
| `clear` | `clear` | Emergency protocol: Evacuates all ships immediately. |
| `help` | `help` | Displays the list of available commands. |
| `exit` | `exit` | Saves the current state to disk and shuts down the system. |

## 🏗 Technical Architecture
*   **Model**: `Spaceship` (Entity), `Spaceport` (Manager).
*   **Controller**: `Main` class running the event loop.
*   **Commands**: `DockCommand`, `UndockCommand`, etc., implementing the `Command` interface.
*   **Data**: Stored in `spaceport_data.csv`.

## 🚀 How to Run
1.  **Compile & Run**:
    Double-click `run_lab5.bat` (Windows) or run the following in your terminal:
    ```bash
    javac -d bin src/com/spaceport/*.java src/com/spaceport/model/*.java src/com/spaceport/command/*.java
    java -cp bin com.spaceport.Main
    ```

## 🔮 Future Upgrades
*   **Lab 6**: Network connectivity (Client-Server architecture).
*   **Lab 7**: Database integration (PostgreSQL) and Security.
*   **Lab 8**: Graphical User Interface (JavaFX/Swing).

---
*Deep Space 99 - Keeping the galaxy moving, one ship at a time.*

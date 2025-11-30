# 🌌 Deep Space 99: Spaceport Network (Lab 6 Task)

## 🚀 Mission Overview
Welcome back, Chief Traffic Controller! The Spaceport has evolved. We have transitioned from a local console application to a **Distributed Client-Server System**. You can now manage the spaceport remotely via a network connection.

This project represents **Lab 6 Task**, focusing on network programming, non-blocking I/O, and distributed architecture.

## 🎯 Objectives
In this module (Lab 6 Task), we expand on the foundations of Lab 5:
*   **Client-Server Architecture**: Splitting the application into a `Server` (logic/data) and `Client` (UI).
*   **Java NIO**: Using `Selector`, `ServerSocketChannel`, and `SocketChannel` for efficient, non-blocking network communication.
*   **Serialization**: Transmitting objects (`Request`, `Response`, `Spaceship`) over the network.
*   **Remote Command Execution**: Commands are now executed on the server, with results sent back to the client.

## 🛠 Features & Commands
The system still operates via a robust command-line interface (CLI), but now over a network.

| Command | Usage | Description |
| :--- | :--- | :--- |
| `dock` | `dock [id] [name] [tonnage]` | Registers a new ship to the docking bay (Server-side). |
| `undock` | `undock [id]` | Clears a ship from the dock and logs its departure. |
| `list` | `list` | Displays the current manifest of all docked ships (fetched from Server). |
| `sort_heavy` | `sort_heavy` | Reorders the manifest by tonnage (heaviest first). |
| `clear` | `clear` | Emergency protocol: Evacuates all ships immediately. |
| `help` | `help` | Displays the list of available commands. |
| `exit` | `exit` | Disconnects the client. |

## 🏗 Technical Architecture
The project is now modularized:

### 1. Common Module
*   **`Spaceship`**: The core entity (Serializable).
*   **`Request`**: DTO containing command name, arguments, and payload.
*   **`Response`**: DTO containing success status, message, and data.

### 2. Server Module (`com.spaceport.server`)
*   **`Server`**: Uses Java NIO `Selector` to handle multiple clients on port 5000.
*   **`Spaceport`**: Manages the collection and file persistence (`spaceport_data.csv`).
*   **Command Execution**: Receives `Request`, executes logic, returns `Response`.

### 3. Client Module (`com.spaceport.client`)
*   **`Client`**: Connects to the server, reads user input, sends `Request` objects, and displays `Response` objects.

## 🚀 How to Run
You need to run the Server first, then one or more Clients.

### 1. Start the Server
Double-click `run_server.bat` (Windows).
*   This compiles the project and starts the server on port 5000.
*   The server loads data from `spaceport_data.csv`.

### 2. Start the Client
Double-click `run_client.bat` (Windows).
*   This compiles the client and connects to `localhost:5000`.
*   You can open multiple client windows to simulate a busy network.

## 🔄 Changes from Lab 5
*   **Split into Modules**: Code separated into `server`, `client`, and `common` packages.
*   **Networked**: Replaced local `Main` loop with `Server` listener and `Client` sender.
*   **Protocol**: Introduced `Request` and `Response` objects for structured communication.
*   **Non-Blocking I/O**: Server uses `Selector` to handle connections efficiently without a thread-per-client model.

## 🔮 Future Upgrades
*   **Lab 7**: Database integration (PostgreSQL) and Security.
*   **Lab 8**: Graphical User Interface (JavaFX/Swing).

---
*Deep Space 99 - Keeping the galaxy moving, one ship at a time.*

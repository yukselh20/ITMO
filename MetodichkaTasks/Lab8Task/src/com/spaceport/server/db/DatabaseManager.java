package com.spaceport.server.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static String DB_URL;
    private static String USER;
    private static String PASS;

    static {
        java.util.Properties props = new java.util.Properties();
        try (java.io.FileInputStream fis = new java.io.FileInputStream("db.properties")) {
            props.load(fis);
        } catch (java.io.IOException e) {
            System.out.println("No db.properties found, checking environment variables.");
        }

        DB_URL = props.getProperty("db.url", System.getenv("DB_URL"));
        USER = props.getProperty("db.user", System.getenv("DB_USER"));
        PASS = props.getProperty("db.password", System.getenv("DB_PASS"));

        if (DB_URL == null || USER == null || PASS == null) {
            throw new RuntimeException(
                    "Database configuration missing! Set db.properties or environment variables (DB_URL, DB_USER, DB_PASS).");
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    public static void initDatabase() {
        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement()) {

            // Create Users table
            String createUsersTable = "CREATE TABLE IF NOT EXISTS users (" +
                    "id SERIAL PRIMARY KEY, " +
                    "username VARCHAR(50) UNIQUE NOT NULL, " +
                    "password_hash VARCHAR(255) NOT NULL" +
                    ")";
            stmt.execute(createUsersTable);

            // Create Spaceships table
            // Create Spaceships table
            String createSpaceshipsTable = "CREATE TABLE IF NOT EXISTS spaceships (" +
                    "id VARCHAR(50) PRIMARY KEY, " +
                    "name VARCHAR(100) NOT NULL, " +
                    "tonnage DOUBLE PRECISION NOT NULL, " +
                    "type VARCHAR(50) NOT NULL, " +
                    "owner_id INTEGER REFERENCES users(id), " +
                    "x DOUBLE PRECISION DEFAULT 0, " +
                    "y DOUBLE PRECISION DEFAULT 0" +
                    ")";
            stmt.execute(createSpaceshipsTable);

            // Migration: Add columns if they don't exist (for existing DBs)
            try {
                stmt.execute("ALTER TABLE spaceships ADD COLUMN IF NOT EXISTS x DOUBLE PRECISION DEFAULT 0");
                stmt.execute("ALTER TABLE spaceships ADD COLUMN IF NOT EXISTS y DOUBLE PRECISION DEFAULT 0");
            } catch (SQLException e) {
                // Ignore if columns already exist or other minor issues, but log it
                System.out.println("Migration check: " + e.getMessage());
            }

            System.out.println("Database initialized successfully.");

        } catch (SQLException e) {
            System.err.println("Database initialization failed: " + e.getMessage());
        }
    }
}

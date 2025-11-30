package com.spaceport.server.db;

import com.spaceport.common.Spaceship;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SpaceportDAO {

    public List<Spaceship> loadAllShips() {
        List<Spaceship> ships = new ArrayList<>();
        String sql = "SELECT id, name, tonnage, type, owner_id FROM spaceships";
        try (Connection conn = DatabaseManager.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ships.add(new Spaceship(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getDouble("tonnage"),
                        rs.getString("type"),
                        rs.getInt("owner_id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ships;
    }

    public boolean addShip(Spaceship ship) {
        String sql = "INSERT INTO spaceships (id, name, tonnage, type, owner_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ship.getId());
            pstmt.setString(2, ship.getName());
            pstmt.setDouble(3, ship.getTonnage());
            pstmt.setString(4, ship.getType());
            pstmt.setInt(5, ship.getOwnerId());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeShip(String id) {
        String sql = "DELETE FROM spaceships WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void clearAll() {
        String sql = "DELETE FROM spaceships";
        try (Connection conn = DatabaseManager.getConnection();
                Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

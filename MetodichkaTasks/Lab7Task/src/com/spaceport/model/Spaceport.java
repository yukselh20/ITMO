package com.spaceport.model;

import com.spaceport.command.Command;
import com.spaceport.common.Request;
import com.spaceport.common.Response;
import com.spaceport.common.Spaceship;
import com.spaceport.server.db.SpaceportDAO;
import com.spaceport.server.db.UserDAO;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Spaceport {
    // Use ConcurrentHashMap for thread safety as per Lab 7 requirements
    private final Map<String, Spaceship> dockedShips = new ConcurrentHashMap<>();
    private final Map<String, Command> commands = new HashMap<>();
    private final UserDAO userDAO;
    private final SpaceportDAO spaceportDAO;

    public Spaceport() {
        this.userDAO = new UserDAO();
        this.spaceportDAO = new SpaceportDAO();
    }

    public void registerCommand(Command command) {
        commands.put(command.getName(), command);
    }

    public Set<String> getCommandNames() {
        return Collections.unmodifiableSet(commands.keySet());
    }

    public Command getCommand(String name) {
        return commands.get(name);
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public Response executeCommand(Request request, int userId) {
        Command cmd = commands.get(request.getCommandName());
        if (cmd == null) {
            return new Response(false, "Unknown command: " + request.getCommandName(), null);
        }

        String[] args = request.getArgument() != null && !request.getArgument().isBlank()
                ? request.getArgument().split("\\s+")
                : new String[0];

        return cmd.execute(args, request.getPayload(), userId);
    }

    public boolean dockShip(Spaceship ship) {
        if (dockedShips.containsKey(ship.getId())) {
            return false;
        }
        // Save to DB first
        if (spaceportDAO.addShip(ship)) {
            dockedShips.put(ship.getId(), ship);
            return true;
        }
        return false;
    }

    public boolean undockShip(String id) {
        if (!dockedShips.containsKey(id)) {
            return false;
        }
        // Remove from DB first
        if (spaceportDAO.removeShip(id)) {
            dockedShips.remove(id);
            return true;
        }
        return false;
    }

    public Spaceship getShip(String id) {
        return dockedShips.get(id);
    }

    public List<Spaceship> getAllShips() {
        return new ArrayList<>(dockedShips.values());
    }

    public void sortShipsByTonnage() {
        // Sorting doesn't affect the Map, but we can return a sorted list if needed.
        // The SortHeavyCommand will handle the sorting logic on the returned list.
    }

    public void clearAll() {
        spaceportDAO.clearAll();
        dockedShips.clear();
    }

    public void loadFromDB() {
        List<Spaceship> ships = spaceportDAO.loadAllShips();
        dockedShips.clear();
        for (Spaceship ship : ships) {
            dockedShips.put(ship.getId(), ship);
        }
        System.out.println("Loaded " + ships.size() + " ships from database.");
    }
}

package com.spaceport.model;

import com.spaceport.command.Command;
import com.spaceport.common.Request;
import com.spaceport.common.Response;
import com.spaceport.common.Spaceship;

import java.io.*;
import java.util.*;

public class Spaceport {
    private final Set<String> shipIds = new HashSet<>();
    private final List<Spaceship> dockedShips = new ArrayList<>();
    private final Map<String, Command> commands = new HashMap<>();
    private final String dataFile = "spaceport_data.csv";

    public void registerCommand(Command command) {
        commands.put(command.getName(), command);
    }

    public Set<String> getCommandNames() {
        return Collections.unmodifiableSet(commands.keySet());
    }

    public Command getCommand(String name) {
        return commands.get(name);
    }

    public Response executeCommand(Request request) {
        Command cmd = commands.get(request.getCommandName());
        if (cmd == null) {
            return new Response(false, "Unknown command: " + request.getCommandName(), null);
        }

        String[] args = request.getArgument() != null && !request.getArgument().isBlank()
                ? request.getArgument().split("\\s+")
                : new String[0];

        return cmd.execute(args, request.getPayload());
    }

    public boolean dockShip(Spaceship ship) {
        if (shipIds.contains(ship.getId())) {
            return false;
        }
        shipIds.add(ship.getId());
        dockedShips.add(ship);
        return true;
    }

    public boolean undockShip(String id) {
        if (!shipIds.contains(id)) {
            return false;
        }
        shipIds.remove(id);
        dockedShips.removeIf(s -> s.getId().equals(id));
        return true;
    }

    public List<Spaceship> getAllShips() {
        return new ArrayList<>(dockedShips);
    }

    public void sortShipsByTonnage() {
        Collections.sort(dockedShips);
    }

    public void clearAll() {
        shipIds.clear();
        dockedShips.clear();
    }

    public void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(dataFile))) {
            for (Spaceship ship : dockedShips) {
                writer.printf("%s,%s,%.2f,%s%n", ship.getId(), ship.getName(), ship.getTonnage(), ship.getType());
            }
            System.out.println("Data saved to " + dataFile);
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    public void loadFromFile() {
        File file = new File(dataFile);
        if (!file.exists())
            return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String id = parts[0];
                    String name = parts[1];
                    double tonnage = Double.parseDouble(parts[2]);
                    String type = parts[3];
                    dockShip(new Spaceship(id, name, tonnage, type));
                }
            }
            System.out.println("Data loaded from " + dataFile);
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading data: " + e.getMessage());
        }
    }
}

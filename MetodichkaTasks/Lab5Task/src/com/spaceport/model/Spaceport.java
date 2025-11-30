package com.spaceport.model;

import com.spaceport.command.Command;
import java.io.*;
import java.util.*;

/**
 * The main manager class for the Spaceport.
 * Handles ship storage, ID uniqueness, command registration, and file
 * persistence.
 */
public class Spaceport {
    private final HashSet<String> shipIds;
    private final ArrayList<Spaceship> dockedShips;
    private final HashMap<String, Command> commands;
    private final String dataFile = "spaceport_data.csv";

    public Spaceport() {
        this.shipIds = new HashSet<>();
        this.dockedShips = new ArrayList<>();
        this.commands = new HashMap<>();
    }

    // --- Command Management ---

    public void registerCommand(Command command) {
        commands.put(command.getName(), command);
    }

    public Set<String> getCommandNames() {
        return Collections.unmodifiableSet(commands.keySet());
    }

    public Command getCommand(String name) {
        return commands.get(name);
    }

    public String executeCommand(String input) {
        String[] parts = input.trim().split("\\s+");
        if (parts.length == 0)
            return "";

        String commandName = parts[0];
        Command command = commands.get(commandName);

        if (command == null) {
            return "Error: Unknown command '" + commandName + "'. Type 'help' for a list of commands."; // Optional help
                                                                                                        // hint
        }

        // Pass arguments (excluding command name)
        String[] args = Arrays.copyOfRange(parts, 1, parts.length);
        return command.execute(args);
    }

    // --- Ship Management ---

    public boolean dockShip(Spaceship ship) {
        if (shipIds.contains(ship.getId())) {
            return false; // ID collision
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
        return new ArrayList<>(dockedShips); // Return copy to protect internal list
    }

    public void clearAll() {
        shipIds.clear();
        dockedShips.clear();
    }

    public void sortShipsByTonnage() {
        Collections.sort(dockedShips);
    }

    // --- Persistence ---

    public void loadFromFile() {
        File file = new File(dataFile);
        if (!file.exists())
            return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String id = parts[0];
                    String name = parts[1];
                    double tonnage = Double.parseDouble(parts[2]);
                    String type = parts[3];
                    dockShip(new Spaceship(id, name, tonnage, type));
                }
            }
            System.out.println("Loaded data from " + dataFile);
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading data: " + e.getMessage());
        }
    }

    public void saveToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(dataFile))) {
            for (Spaceship ship : dockedShips) {
                bw.write(String.format("%s,%s,%.2f,%s",
                        ship.getId(), ship.getName(), ship.getTonnage(), ship.getType()));
                bw.newLine();
            }
            System.out.println("Saving data to " + dataFile + "...");
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }
}

package com.spaceport.command;

import com.spaceport.model.Spaceport;
import com.spaceport.model.Spaceship;

public class DockCommand implements Command {
    private final Spaceport spaceport;

    public DockCommand(Spaceport spaceport) {
        this.spaceport = spaceport;
    }

    @Override
    public String getName() {
        return "dock";
    }

    @Override
    public String getDescription() {
        return "dock [id] [name] [tonnage] - Create and add a ship.";
    }

    @Override
    public String execute(String[] args) {
        if (args.length != 3) {
            return "Usage: dock [id] [name] [tonnage]";
        }

        String id = args[0];
        String name = args[1];
        double tonnage;

        try {
            tonnage = Double.parseDouble(args[2]);
        } catch (NumberFormatException e) {
            return "Error: Tonnage must be a number.";
        }

        try {
            Spaceship ship = new Spaceship(id, name, tonnage, "Cargo");
            if (spaceport.dockShip(ship)) {
                return String.format("Success: %s (%s) has docked.", name, id);
            } else {
                return "Error: A ship with ID " + id + " is already docked!";
            }
        } catch (IllegalArgumentException e) {
            return "Error: " + e.getMessage();
        }
    }
}

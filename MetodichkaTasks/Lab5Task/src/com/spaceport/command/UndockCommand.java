package com.spaceport.command;

import com.spaceport.model.Spaceport;

public class UndockCommand implements Command {
    private final Spaceport spaceport;

    public UndockCommand(Spaceport spaceport) {
        this.spaceport = spaceport;
    }

    @Override
    public String getName() {
        return "undock";
    }

    @Override
    public String getDescription() {
        return "undock [id] - Remove a ship by its ID.";
    }

    @Override
    public String execute(String[] args) {
        if (args.length != 1) {
            return "Usage: undock [id]";
        }

        String id = args[0];
        if (spaceport.undockShip(id)) {
            return "Success: Ship " + id + " has undocked.";
        } else {
            return "Error: Ship with ID " + id + " not found.";
        }
    }
}

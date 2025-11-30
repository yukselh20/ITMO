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
    public com.spaceport.common.Response execute(String[] args, Object payload) {
        // Undock still uses ID from args (or could be payload, but args is fine for
        // simple string)
        if (args.length != 1) {
            // If args are empty, check if payload is a string ID? No, let's stick to args
            // for simple values.
            // Actually, Request DTO has 'argument' string.
            return new com.spaceport.common.Response(false, "Usage: undock [id]", null);
        }

        String id = args[0];
        if (spaceport.undockShip(id)) {
            return new com.spaceport.common.Response(true, "Success: Ship " + id + " has undocked.", null);
        } else {
            return new com.spaceport.common.Response(false, "Error: Ship with ID " + id + " not found.", null);
        }
    }
}

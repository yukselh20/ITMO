package com.spaceport.command;

import com.spaceport.model.Spaceport;
import com.spaceport.common.Response;
import com.spaceport.common.Spaceship;

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
    public Response execute(String[] args, Object payload, int userId) {
        if (args.length != 1) {
            return new Response(false, "Usage: undock [id]", null);
        }

        String id = args[0];

        Spaceship ship = spaceport.getShip(id);
        if (ship == null) {
            return new Response(false, "Error: Ship with ID " + id + " not found.", null);
        }

        if (ship.getOwnerId() != userId) {
            return new Response(false, "Error: Access Denied. You do not own this ship.", null);
        }

        if (spaceport.undockShip(id)) {
            return new Response(true, "Success: Ship " + id + " has undocked.", null);
        } else {
            return new Response(false, "Error: Ship with ID " + id + " not found.", null);
        }
    }
}

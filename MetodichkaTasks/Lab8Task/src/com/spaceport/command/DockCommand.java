package com.spaceport.command;

import com.spaceport.model.Spaceport;
import com.spaceport.common.Response;
import com.spaceport.common.Spaceship;

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
    public Response execute(String[] args, Object payload, int userId) {
        // In Lab 6 Task, the client sends the Spaceship object as payload.
        if (!(payload instanceof Spaceship)) {
            return new Response(false, "Error: Invalid payload for dock command.", null);
        }

        if (userId == 0) {
            return new Response(false, "Error: You must be logged in to dock a ship.", null);
        }

        Spaceship ship = (Spaceship) payload;

        // Create a new Spaceship with the correct owner ID, preserving X and Y
        Spaceship shipWithOwner = new Spaceship(ship.getId(), ship.getName(), ship.getTonnage(), ship.getType(),
                userId, ship.getX(), ship.getY());

        if (spaceport.dockShip(shipWithOwner)) {
            return new Response(true,
                    String.format("Success: %s (%s) has docked. Owner ID: %d", shipWithOwner.getName(),
                            shipWithOwner.getId(), userId),
                    null);
        } else {
            return new Response(false,
                    "Error: A ship with ID " + ship.getId() + " is already docked!", null);
        }
    }
}

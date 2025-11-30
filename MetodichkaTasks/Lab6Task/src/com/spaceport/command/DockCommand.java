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
    public Response execute(String[] args, Object payload) {
        // In Lab 6 Task, the client sends the Spaceship object as payload.
        if (!(payload instanceof Spaceship)) {
            return new Response(false, "Error: Invalid payload for dock command.", null);
        }

        Spaceship ship = (Spaceship) payload;

        if (spaceport.dockShip(ship)) {
            return new Response(true,
                    String.format("Success: %s (%s) has docked.", ship.getName(), ship.getId()), null);
        } else {
            return new Response(false,
                    "Error: A ship with ID " + ship.getId() + " is already docked!", null);
        }
    }
}

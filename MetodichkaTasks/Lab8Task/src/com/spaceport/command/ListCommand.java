package com.spaceport.command;

import com.spaceport.common.Response;
import com.spaceport.common.Spaceship;
import com.spaceport.model.Spaceport;
import java.util.List;

public class ListCommand implements Command {
    private final Spaceport spaceport;

    public ListCommand(Spaceport spaceport) {
        this.spaceport = spaceport;
    }

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String getDescription() {
        return "list - List all docked ships.";
    }

    @Override
    public Response execute(String[] args, Object payload, int userId) {
        List<Spaceship> ships = spaceport.getAllShips();
        if (ships.isEmpty()) {
            return new Response(true, "No ships docked.", null);
        }
        StringBuilder sb = new StringBuilder("Docked ships:\n");
        for (Spaceship ship : ships) {
            sb.append(ship).append("\n");
        }
        return new Response(true, sb.toString(), ships);
    }
}

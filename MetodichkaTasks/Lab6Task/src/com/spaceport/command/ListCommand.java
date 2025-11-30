package com.spaceport.command;

import com.spaceport.model.Spaceport;
import com.spaceport.common.Response;
import com.spaceport.common.Spaceship;
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
        return "list - Show all docked ships.";
    }

    @Override
    public Response execute(String[] args, Object payload) {
        List<Spaceship> ships = spaceport.getAllShips();
        if (ships.isEmpty()) {
            return new Response(true, "Spaceport is empty.", ships);
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Docked Ships:\n");
        for (Spaceship ship : ships) {
            sb.append(ship).append("\n");
        }
        // We return the list as data, and the string representation as message.
        return new Response(true, sb.toString().trim(), ships);
    }
}

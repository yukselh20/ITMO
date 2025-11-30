package com.spaceport.command;

import com.spaceport.model.Spaceport;
import com.spaceport.model.Spaceship;
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
    public String execute(String[] args) {
        List<Spaceship> ships = spaceport.getAllShips();
        if (ships.isEmpty()) {
            return "Spaceport is empty.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Docked Ships:\n");
        for (Spaceship ship : ships) {
            sb.append(ship).append("\n");
        }
        return sb.toString().trim();
    }
}

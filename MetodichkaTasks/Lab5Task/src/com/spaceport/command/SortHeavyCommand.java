package com.spaceport.command;

import com.spaceport.model.Spaceport;
import com.spaceport.model.Spaceship;
import java.util.List;

public class SortHeavyCommand implements Command {
    private final Spaceport spaceport;

    public SortHeavyCommand(Spaceport spaceport) {
        this.spaceport = spaceport;
    }

    @Override
    public String getName() {
        return "sort_heavy";
    }

    @Override
    public String getDescription() {
        return "sort_heavy - Display ships sorted by tonnage.";
    }

    @Override
    public String execute(String[] args) {
        spaceport.sortShipsByTonnage();
        List<Spaceship> ships = spaceport.getAllShips();

        if (ships.isEmpty()) {
            return "Spaceport is empty.";
        }

        StringBuilder sb = new StringBuilder();
        int rank = 1;
        for (Spaceship ship : ships) {
            sb.append(String.format("%d. %s (%.1f tons)\n", rank++, ship.getName(), ship.getTonnage()));
        }
        return sb.toString().trim();
    }
}

package com.spaceport.command;

import com.spaceport.common.Response;
import com.spaceport.common.Spaceship;
import com.spaceport.model.Spaceport;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
        return "sort_heavy - Sort ships by tonnage (descending).";
    }

    @Override
    public Response execute(String[] args, Object payload, int userId) {
        if (userId == 0) {
            return new Response(false, "Error: You must be logged in.", null);
        }
        List<Spaceship> ships = spaceport.getAllShips();
        List<Spaceship> sortedShips = ships.stream()
                .sorted(Comparator.comparingDouble(Spaceship::getTonnage).reversed())
                .collect(Collectors.toList());
        return new Response(true, "Ships sorted by tonnage.", sortedShips);
    }
}

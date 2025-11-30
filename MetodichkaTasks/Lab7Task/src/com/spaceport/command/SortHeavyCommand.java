package com.spaceport.command;

import com.spaceport.common.Response;
import com.spaceport.common.Spaceship;
import com.spaceport.model.Spaceport;
import java.util.Collections;
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
        return "sort_heavy - List ships sorted by tonnage (descending).";
    }

    @Override
    public Response execute(String[] args, Object payload, int userId) {
        List<Spaceship> ships = spaceport.getAllShips();
        Collections.sort(ships); // Spaceship implements Comparable (by tonnage desc)

        StringBuilder sb = new StringBuilder("Ships sorted by tonnage:\n");
        for (Spaceship ship : ships) {
            sb.append(ship).append("\n");
        }
        return new Response(true, sb.toString(), ships);
    }
}

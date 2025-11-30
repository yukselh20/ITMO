package com.spaceport.command;

import com.spaceport.common.Response;
import com.spaceport.model.Spaceport;

public class ClearCommand implements Command {
    private final Spaceport spaceport;

    public ClearCommand(Spaceport spaceport) {
        this.spaceport = spaceport;
    }

    @Override
    public String getName() {
        return "clear";
    }

    @Override
    public String getDescription() {
        return "clear - Remove all ships from the spaceport.";
    }

    @Override
    public Response execute(String[] args, Object payload, int userId) {
        if (userId == 0) {
            return new Response(false, "Error: You must be logged in.", null);
        }
        spaceport.clearAll();
        return new Response(true, "All ships cleared.", null);
    }
}

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
        return "clear - Remove all ships.";
    }

    @Override
    public Response execute(String[] args, Object payload, int userId) {
        // Only admin or owner can clear? For now, let's allow it but maybe restrict
        // later.
        // Or maybe clear only own ships?
        // Lab requirements don't specify, but let's assume clear removes ALL ships
        // (admin command).
        // Or maybe we should disable it for now or make it clear only user's ships.
        // Let's make it clear all for simplicity as per previous lab, but maybe add a
        // check?

        spaceport.clearAll();
        return new Response(true, "All ships cleared.", null);
    }
}

package com.spaceport.command;

import com.spaceport.common.Response;
import com.spaceport.model.Spaceport;

public class HelpCommand implements Command {
    private final Spaceport spaceport;

    public HelpCommand(Spaceport spaceport) {
        this.spaceport = spaceport;
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "help - Show available commands.";
    }

    @Override
    public Response execute(String[] args, Object payload, int userId) {
        StringBuilder sb = new StringBuilder("Available commands:\n");
        for (String name : spaceport.getCommandNames()) {
            Command cmd = spaceport.getCommand(name);
            sb.append(String.format("- %s: %s%n", cmd.getName(), cmd.getDescription()));
        }
        return new Response(true, sb.toString(), null);
    }
}

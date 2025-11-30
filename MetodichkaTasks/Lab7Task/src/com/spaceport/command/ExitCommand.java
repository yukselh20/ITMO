package com.spaceport.command;

import com.spaceport.common.Response;

public class ExitCommand implements Command {
    public ExitCommand() {
    }

    @Override
    public String getName() {
        return "exit";
    }

    @Override
    public String getDescription() {
        return "exit - Save and exit.";
    }

    @Override
    public Response execute(String[] args, Object payload, int userId) {
        return new Response(true, "Exiting...", null);
    }
}

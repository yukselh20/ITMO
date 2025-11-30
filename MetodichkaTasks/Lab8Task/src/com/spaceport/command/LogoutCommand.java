package com.spaceport.command;

import com.spaceport.common.Response;

public class LogoutCommand implements Command {
    public LogoutCommand() {
    }

    @Override
    public String getName() {
        return "logout";
    }

    @Override
    public String getDescription() {
        return "logout - Log out of the system.";
    }

    @Override
    public Response execute(String[] args, Object payload, int userId) {
        if (userId == 0) {
            return new Response(false, "Error: You are not logged in.", null);
        }
        return new Response(true, "Logged out successfully.", null);
    }
}

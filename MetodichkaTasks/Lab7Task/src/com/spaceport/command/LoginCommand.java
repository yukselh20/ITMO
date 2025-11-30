package com.spaceport.command;

import com.spaceport.common.Response;
import com.spaceport.common.User;
import com.spaceport.model.Spaceport;
import java.util.Optional;

public class LoginCommand implements Command {
    private final Spaceport spaceport;

    public LoginCommand(Spaceport spaceport) {
        this.spaceport = spaceport;
    }

    @Override
    public String getName() {
        return "login";
    }

    @Override
    public String getDescription() {
        return "login [username] [password] - Login to the system.";
    }

    @Override
    public Response execute(String[] args, Object payload, int userId) {
        if (args.length < 2) {
            return new Response(false, "Usage: login [username] [password]", null);
        }
        String username = args[0];
        String password = args[1];

        if (spaceport.getUserDAO().checkPassword(username, password)) {
            Optional<User> user = spaceport.getUserDAO().getUser(username);
            return user.map(value -> new Response(true, "Login successful.", value))
                    .orElseGet(() -> new Response(false, "Login failed. User not found.", null));
        } else {
            return new Response(false, "Login failed. Invalid credentials.", null);
        }
    }
}

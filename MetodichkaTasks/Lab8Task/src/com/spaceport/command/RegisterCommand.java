package com.spaceport.command;

import com.spaceport.common.Response;
import com.spaceport.common.User;
import com.spaceport.model.Spaceport;
import java.util.Optional;

public class RegisterCommand implements Command {
    private final Spaceport spaceport;

    public RegisterCommand(Spaceport spaceport) {
        this.spaceport = spaceport;
    }

    @Override
    public String getName() {
        return "register";
    }

    @Override
    public String getDescription() {
        return "register [username] [password] - Register a new user.";
    }

    @Override
    public Response execute(String[] args, Object payload, int userId) {
        if (args.length < 2) {
            return new Response(false, "Usage: register [username] [password]", null);
        }
        String username = args[0];
        String password = args[1];

        Optional<User> user = spaceport.getUserDAO().registerUser(username, password);
        if (user.isPresent()) {
            return new Response(true, "Registration successful. User ID: " + user.get().getId(), user.get());
        } else {
            return new Response(false, "Registration failed. Username might be taken.", null);
        }
    }
}

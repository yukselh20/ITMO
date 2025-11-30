package com.spaceport.common;

import java.io.Serializable;

/**
 * Data Transfer Object for client requests.
 */
public class Request implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String commandName;
    private final String argument;
    private final Object payload; // e.g., Spaceship object for dock command

    public Request(String commandName, String argument, Object payload) {
        this.commandName = commandName;
        this.argument = argument;
        this.payload = payload;
    }

    public String getCommandName() {
        return commandName;
    }

    public String getArgument() {
        return argument;
    }

    public Object getPayload() {
        return payload;
    }
}

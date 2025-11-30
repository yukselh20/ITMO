package com.spaceport.command;

import com.spaceport.common.Response;

/**
 * Interface for all console commands.
 */
public interface Command {
    /**
     * @return The name of the command (e.g., "dock").
     */
    String getName();

    /**
     * @return A short description and usage of the command.
     */
    String getDescription();

    /**
     * Executes the command with the given arguments.
     * 
     * @param args    The arguments passed to the command (excluding the command
     *                name).
     * @param payload Optional object payload (e.g., Spaceship).
     * @return A Response object to send back to the client.
     */
    Response execute(String[] args, Object payload);
}

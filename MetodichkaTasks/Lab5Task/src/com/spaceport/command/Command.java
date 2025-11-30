package com.spaceport.command;

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
     * @param args The arguments passed to the command (excluding the command name).
     * @return A result string to display to the user.
     */
    String execute(String[] args);
}

package com.spaceport.command;

import com.spaceport.model.Spaceport;

public class ExitCommand implements Command {
    private final Spaceport spaceport;

    public ExitCommand(Spaceport spaceport) {
        this.spaceport = spaceport;
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
    public String execute(String[] args) {
        spaceport.saveToFile();
        return "Goodbye, Controller.";
    }
}

package com.spaceport.command;

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
    public String execute(String[] args) {
        spaceport.clearAll();
        return "Spaceport cleared.";
    }
}

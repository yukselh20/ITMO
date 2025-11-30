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
    public com.spaceport.common.Response execute(String[] args, Object payload) {
        spaceport.saveToFile();
        return new com.spaceport.common.Response(true, "Goodbye, Controller.", null);
    }
}

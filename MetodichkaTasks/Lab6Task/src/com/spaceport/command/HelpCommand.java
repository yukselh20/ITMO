package com.spaceport.command;

import com.spaceport.model.Spaceport;

public class HelpCommand implements Command {
    private final Spaceport spaceport;

    public HelpCommand(Spaceport spaceport) {
        this.spaceport = spaceport;
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "help - Show available commands and their usage.";
    }

    @Override
    public com.spaceport.common.Response execute(String[] args, Object payload) {
        StringBuilder sb = new StringBuilder();
        sb.append("Available Commands:\n");
        for (String name : spaceport.getCommandNames()) {
            Command cmd = spaceport.getCommand(name);
            if (cmd != null) {
                sb.append(" - ").append(cmd.getDescription()).append("\n");
            }
        }
        return new com.spaceport.common.Response(true, sb.toString().trim(), null);
    }
}

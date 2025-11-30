package com.spaceport;

import com.spaceport.command.*;
import com.spaceport.model.Spaceport;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Spaceport spaceport = new Spaceport();

        // Register Commands
        spaceport.registerCommand(new DockCommand(spaceport));
        spaceport.registerCommand(new UndockCommand(spaceport));
        spaceport.registerCommand(new ListCommand(spaceport));
        spaceport.registerCommand(new SortHeavyCommand(spaceport));
        spaceport.registerCommand(new ClearCommand(spaceport));
        spaceport.registerCommand(new ExitCommand(spaceport));
        spaceport.registerCommand(new HelpCommand(spaceport));

        System.out.println("==========================================");
        System.out.println("   Deep Space 99 - Traffic Control System");
        System.out.println("==========================================");
        System.out.println("Commands: dock, undock, list, sort_heavy, clear, exit");
        System.out.println("Type 'help' for detailed usage.");
        System.out.println("------------------------------------------");
        spaceport.loadFromFile();

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.print("> ");
            if (scanner.hasNextLine()) {
                String input = scanner.nextLine();
                String result = spaceport.executeCommand(input);
                System.out.println(result);

                if (result.equals("Goodbye, Controller.")) {
                    running = false;
                }
            } else {
                running = false;
            }
        }
        scanner.close();
    }
}

package com.spaceport.client;

import com.spaceport.common.Request;
import com.spaceport.common.Response;
import com.spaceport.common.Spaceship;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class Client {
    private final String host = "localhost";
    private final int port = 5000;
    private SocketChannel socketChannel;

    public void start() {
        try {
            socketChannel = SocketChannel.open(new InetSocketAddress(host, port));
            socketChannel.configureBlocking(true); // Blocking for simplicity in client
            System.out.println("Connected to Spaceport Server at " + host + ":" + port);
            System.out.println("Welcome! Please 'login [user] [pass]' or 'register [user] [pass]'.");
            System.out.println("Type 'help' for commands or 'exit' to quit.");

            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.print("> ");
                if (!scanner.hasNextLine())
                    break;
                String input = scanner.nextLine().trim();
                if (input.isEmpty())
                    continue;

                Request request = createRequest(input);
                if (request == null)
                    continue; // Invalid input handled locally

                sendRequest(request);
                Response response = receiveResponse();

                if (response != null) {
                    if (response.isSuccess()) {
                        System.out.println(response.getMessage());
                    } else {
                        System.err.println(response.getMessage());
                    }

                    if (request.getCommandName().equals("exit") && response.isSuccess()) {
                        break;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Connection error: " + e.getMessage());
        }
    }

    private Request createRequest(String input) {
        String[] parts = input.split("\\s+", 2);
        String commandName = parts[0];
        String argument = parts.length > 1 ? parts[1] : "";
        Object payload = null;

        if (commandName.equals("dock")) {
            // dock [id] [name] [tonnage]
            String[] args = argument.split("\\s+");
            if (args.length != 3) {
                System.out.println("Usage: dock [id] [name] [tonnage]");
                return null;
            }
            try {
                String id = args[0];
                String name = args[1];
                double tonnage = Double.parseDouble(args[2]);
                payload = new Spaceship(id, name, tonnage, "Cargo", 0);
            } catch (NumberFormatException e) {
                System.out.println("Error: Tonnage must be a number.");
                return null;
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
                return null;
            }
        }

        return new Request(commandName, argument, payload);
    }

    private void sendRequest(Request request) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(request);
            oos.flush();

            byte[] data = baos.toByteArray();
            ByteBuffer buffer = ByteBuffer.wrap(data);
            socketChannel.write(buffer);
        } catch (IOException e) {
            System.err.println("Error sending request: " + e.getMessage());
        }
    }

    private Response receiveResponse() {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(4096);
            int bytesRead = socketChannel.read(buffer);
            if (bytesRead == -1) {
                System.out.println("Server closed connection.");
                return null;
            }

            byte[] data = new byte[bytesRead];
            System.arraycopy(buffer.array(), 0, data, 0, bytesRead);

            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
            return (Response) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error receiving response: " + e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        new Client().start();
    }
}

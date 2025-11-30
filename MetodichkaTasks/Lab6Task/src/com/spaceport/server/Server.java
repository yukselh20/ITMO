package com.spaceport.server;

import com.spaceport.command.*;
import com.spaceport.common.Request;
import com.spaceport.common.Response;
import com.spaceport.model.Spaceport;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class Server {
    private final Spaceport spaceport;
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private final int port = 5000;

    public Server() {
        this.spaceport = new Spaceport();
        registerCommands();
        spaceport.loadFromFile();
    }

    private void registerCommands() {
        spaceport.registerCommand(new DockCommand(spaceport));
        spaceport.registerCommand(new UndockCommand(spaceport));
        spaceport.registerCommand(new ListCommand(spaceport));
        spaceport.registerCommand(new SortHeavyCommand(spaceport));
        spaceport.registerCommand(new ClearCommand(spaceport));
        spaceport.registerCommand(new HelpCommand(spaceport));
        spaceport.registerCommand(new ExitCommand(spaceport));
    }

    public void start() {
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(port));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("Server started on port " + port);

            while (true) {
                if (selector.select() == 0)
                    continue;

                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iter = selectedKeys.iterator();

                while (iter.hasNext()) {
                    SelectionKey key = iter.next();
                    iter.remove();

                    if (!key.isValid())
                        continue;

                    if (key.isAcceptable()) {
                        acceptConnection(key);
                    } else if (key.isReadable()) {
                        readRequest(key);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void acceptConnection(SelectionKey key) throws IOException {
        ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
        SocketChannel clientChannel = serverChannel.accept();
        clientChannel.configureBlocking(false);
        clientChannel.register(selector, SelectionKey.OP_READ);
        System.out.println("New client connected: " + clientChannel.getRemoteAddress());
    }

    private void readRequest(SelectionKey key) {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(4096);

        try {
            int bytesRead = clientChannel.read(buffer);
            if (bytesRead == -1) {
                closeClient(clientChannel);
                return;
            }

            byte[] data = new byte[bytesRead];
            System.arraycopy(buffer.array(), 0, data, 0, bytesRead);

            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
            Request request = (Request) ois.readObject();

            System.out.println("Received request: " + request.getCommandName());

            Response response = spaceport.executeCommand(request);

            sendResponse(clientChannel, response);

            if (request.getCommandName().equals("exit")) {
                System.out.println("Client requested exit. Closing connection.");
                closeClient(clientChannel);
            }

        } catch (IOException e) {
            // Connection reset or other IO error usually means client disconnected
            System.out.println("Client disconnected unexpectedly: " + e.getMessage());
            try {
                closeClient(clientChannel);
            } catch (IOException ex) {
                // Ignore
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Error deserializing request: " + e.getMessage());
        }
    }

    private void sendResponse(SocketChannel clientChannel, Response response) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(response);
        oos.flush();

        byte[] data = baos.toByteArray();
        ByteBuffer buffer = ByteBuffer.wrap(data);
        while (buffer.hasRemaining()) {
            clientChannel.write(buffer);
        }
    }

    private void closeClient(SocketChannel clientChannel) throws IOException {
        System.out.println("Closing client connection: " + clientChannel.getRemoteAddress());
        clientChannel.close();
    }

    public static void main(String[] args) {
        new Server().start();
    }
}

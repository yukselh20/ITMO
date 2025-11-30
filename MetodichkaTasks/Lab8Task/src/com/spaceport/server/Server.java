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
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private final Spaceport spaceport;
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private final int port = 5000;
    private final Map<SocketChannel, Integer> sessions = new ConcurrentHashMap<>();
    private final ExecutorService threadPool = Executors.newCachedThreadPool();

    public Server() {
        this.spaceport = new Spaceport();
        registerCommands();
        spaceport.loadFromDB();
    }

    private void registerCommands() {
        spaceport.registerCommand(new DockCommand(spaceport));
        spaceport.registerCommand(new UndockCommand(spaceport));
        spaceport.registerCommand(new ListCommand(spaceport));
        spaceport.registerCommand(new SortHeavyCommand(spaceport));
        spaceport.registerCommand(new ClearCommand(spaceport));
        spaceport.registerCommand(new HelpCommand(spaceport));
        spaceport.registerCommand(new ExitCommand());
        spaceport.registerCommand(new RegisterCommand(spaceport));
        spaceport.registerCommand(new LoginCommand(spaceport));
        spaceport.registerCommand(new LogoutCommand());
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

            // Submit task to thread pool
            threadPool.submit(() -> {
                try {
                    ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
                    Request request = (Request) ois.readObject();

                    System.out.println("Received request: " + request.getCommandName());

                    int userId = sessions.getOrDefault(clientChannel, 0);
                    String cmdName = request.getCommandName();

                    // Enforce Authentication
                    boolean isPublicCmd = cmdName.equals("login") || cmdName.equals("register")
                            || cmdName.equals("help") || cmdName.equals("exit");
                    if (userId == 0 && !isPublicCmd) {
                        sendResponse(clientChannel,
                                new Response(false, "Access Denied: Please 'login' or 'register' first.", null));
                        return;
                    }

                    Response response = spaceport.executeCommand(request, userId);

                    // Handle Login/Register success to update session
                    if (response.isSuccess()) {
                        if (cmdName.equals("login") || cmdName.equals("register")) {
                            if (response.getData() instanceof com.spaceport.common.User) {
                                com.spaceport.common.User user = (com.spaceport.common.User) response.getData();
                                sessions.put(clientChannel, user.getId());
                                System.out.println(
                                        "User logged in: " + user.getUsername() + " (ID: " + user.getId() + ")");
                            }
                        } else if (cmdName.equals("logout")) {
                            sessions.remove(clientChannel);
                            System.out.println("User logged out (ID: " + userId + ")");
                        }
                    }

                    sendResponse(clientChannel, response);

                    if (cmdName.equals("exit")) {
                        System.out.println("Client requested exit. Closing connection.");
                        closeClient(clientChannel);
                    }
                } catch (Exception e) {
                    System.err.println("Error processing request: " + e.getMessage());
                }
            });

        } catch (IOException e) {
            System.out.println("Client disconnected unexpectedly: " + e.getMessage());
            try {
                closeClient(clientChannel);
            } catch (IOException ex) {
                // Ignore
            }
        }
    }

    private void sendResponse(SocketChannel clientChannel, Response response) throws IOException {
        // Synchronize to prevent concurrent writes to the same channel
        synchronized (clientChannel) {
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
    }

    private void closeClient(SocketChannel clientChannel) throws IOException {
        System.out.println("Closing client connection: " + clientChannel.getRemoteAddress());
        sessions.remove(clientChannel);
        clientChannel.close();
    }

    public static void main(String[] args) {
        // Initialize DB
        com.spaceport.server.db.DatabaseManager.initDatabase();
        new Server().start();
    }
}

package com.spaceport.client.controller;

import com.spaceport.client.util.ResourceManager;
import com.spaceport.common.Request;
import com.spaceport.common.Response;
import com.spaceport.common.Spaceship;

import javax.swing.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ClientController {
    private SocketChannel socketChannel;
    private final String host = "localhost";
    private final int port = 5000;
    private int currentUserId = 0;
    private String currentUsername = null;

    public interface ResponseCallback {
        void onSuccess(String message, Object data);

        void onError(String message);
    }

    public void connect() throws IOException {
        socketChannel = SocketChannel.open(new InetSocketAddress(host, port));
        socketChannel.configureBlocking(true);
    }

    public boolean isConnected() {
        return socketChannel != null && socketChannel.isConnected();
    }

    public int getCurrentUserId() {
        return currentUserId;
    }

    public String getCurrentUsername() {
        return currentUsername;
    }

    public void login(String username, String password, ResponseCallback callback) {
        executeRequest(new Request("login", username + " " + password, null), new ResponseCallback() {
            @Override
            public void onSuccess(String msg, Object data) {
                // Parse user ID from success message or assume success means logged in
                // The server returns "Login successful. User ID: X"
                try {
                    String[] parts = msg.split("ID: ");
                    if (parts.length > 1) {
                        currentUserId = Integer.parseInt(parts[1].trim());
                        currentUsername = username;
                    }
                    callback.onSuccess(msg, data);
                } catch (Exception e) {
                    callback.onError("Error parsing login response.");
                }
            }

            @Override
            public void onError(String message) {
                callback.onError(message);
            }
        }, callback);
    }

    public void register(String username, String password, ResponseCallback callback) {
        executeRequest(new Request("register", username + " " + password, null), callback, callback);
    }

    public void logout() {
        executeRequest(new Request("logout", "", null), new ResponseCallback() {
            @Override
            public void onSuccess(String msg, Object data) {
                currentUserId = 0;
                currentUsername = null;
            }

            @Override
            public void onError(String message) {
                currentUserId = 0; // Force logout locally even if server fails
                currentUsername = null;
            }
        }, new ResponseCallback() {
            @Override
            public void onSuccess(String msg, Object data) {
                // Should not happen for error callback
            }

            @Override
            public void onError(String message) {
                currentUserId = 0; // Force logout locally even if server fails
                currentUsername = null;
            }
        });
    }

    public void getShips(ResponseCallback callback) {
        executeRequest(new Request("list", "", null), new ResponseCallback() {
            @Override
            public void onSuccess(String msg, Object data) {
                if (data instanceof List) {
                    callback.onSuccess(msg, data);
                } else {
                    callback.onError("Invalid data format from server.");
                }
            }

            @Override
            public void onError(String message) {
                callback.onError(message);
            }
        }, callback);
    }

    public void dockShip(Spaceship ship, ResponseCallback callback) {
        executeRequest(new Request("dock", "", ship), callback, callback);
    }

    public void undockShip(String shipId, ResponseCallback callback) {
        executeRequest(new Request("undock", shipId, null), callback, callback);
    }

    public void sortShips(ResponseCallback callback) {
        executeRequest(new Request("sort_heavy", "", null), new ResponseCallback() {
            @Override
            public void onSuccess(String msg, Object data) {
                if (data instanceof List) {
                    callback.onSuccess(msg, data);
                } else {
                    callback.onError("Invalid data format from server.");
                }
            }

            @Override
            public void onError(String message) {
                callback.onError(message);
            }
        }, callback);
    }

    public void clearShips(ResponseCallback callback) {
        executeRequest(new Request("clear", "", null), callback, callback);
    }

    public void getHelp(ResponseCallback callback) {
        executeRequest(new Request("help", "", null), callback, callback);
    }

    public void exitApp() {
        try {
            sendRequest(new Request("exit", "", null));
            if (socketChannel != null) {
                socketChannel.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void executeRequest(Request request, ResponseCallback success, ResponseCallback error) {
        new SwingWorker<Response, Void>() {
            @Override
            protected Response doInBackground() throws Exception {
                if (!isConnected()) {
                    throw new IOException(ResourceManager.get("error.connect.failed"));
                }
                sendRequest(request);
                return receiveResponse();
            }

            @Override
            protected void done() {
                try {
                    Response response = get();
                    if (response != null) {
                        if (response.isSuccess()) {
                            success.onSuccess(response.getMessage(), response.getData());
                        } else {
                            error.onError(response.getMessage());
                        }
                    } else {
                        error.onError("No response from server.");
                    }
                } catch (InterruptedException | ExecutionException e) {
                    error.onError(e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
                }
            }
        }.execute();
    }

    private synchronized void sendRequest(Request request) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(request);
        oos.flush();

        byte[] data = baos.toByteArray();
        ByteBuffer buffer = ByteBuffer.wrap(data);
        socketChannel.write(buffer);
    }

    private synchronized Response receiveResponse() throws IOException, ClassNotFoundException {
        ByteBuffer buffer = ByteBuffer.allocate(16384); // Larger buffer for lists
        int bytesRead = socketChannel.read(buffer);
        if (bytesRead == -1) {
            throw new IOException("Server closed connection.");
        }

        byte[] data = new byte[bytesRead];
        System.arraycopy(buffer.array(), 0, data, 0, bytesRead);

        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
        return (Response) ois.readObject();
    }
}

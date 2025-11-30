package com.spaceport.client;

import com.spaceport.client.controller.ClientController;
import com.spaceport.client.gui.LoginFrame;

import javax.swing.*;

public class GuiClient {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set Look and Feel
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            ClientController controller = new ClientController();
            try {
                controller.connect();
                new LoginFrame(controller).setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Could not connect to server: " + e.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }
}

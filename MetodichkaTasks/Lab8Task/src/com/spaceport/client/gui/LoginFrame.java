package com.spaceport.client.gui;

import com.spaceport.client.controller.ClientController;
import com.spaceport.client.util.ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;

public class LoginFrame extends JFrame {
    private final ClientController controller;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> languageBox;
    private JButton loginButton;
    private JButton registerButton;
    private JLabel usernameLabel;
    private JLabel passwordLabel;

    public LoginFrame(ClientController controller) {
        this.controller = controller;
        setTitle(ResourceManager.get("login.title"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 250);
        setLocationRelativeTo(null);
        setResizable(false);

        initComponents();
        updateTexts();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Language Selector
        languageBox = new JComboBox<>(new String[] { "English", "Türkçe" });
        languageBox.addActionListener(e -> {
            String selected = (String) languageBox.getSelectedItem();
            if ("Türkçe".equals(selected)) {
                ResourceManager.setLocale(new Locale("tr", "TR"));
            } else {
                ResourceManager.setLocale(new Locale("en", "US"));
            }
            updateTexts();
        });
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(languageBox, gbc);

        // Username
        usernameLabel = new JLabel();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        mainPanel.add(usernameLabel, gbc);

        usernameField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 1;
        mainPanel.add(usernameField, gbc);

        // Password
        passwordLabel = new JLabel();
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        gbc.gridy = 2;
        mainPanel.add(passwordField, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        loginButton = new JButton();
        registerButton = new JButton();

        loginButton.addActionListener(e -> handleLogin());
        registerButton.addActionListener(e -> handleRegister());

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        mainPanel.add(buttonPanel, gbc);

        add(mainPanel);
    }

    private void updateTexts() {
        setTitle(ResourceManager.get("login.title"));
        usernameLabel.setText(ResourceManager.get("login.username"));
        passwordLabel.setText(ResourceManager.get("login.password"));
        loginButton.setText(ResourceManager.get("login.button"));
        registerButton.setText(ResourceManager.get("register.button"));
    }

    private void handleLogin() {
        String user = usernameField.getText();
        String pass = new String(passwordField.getPassword());
        if (user.isEmpty() || pass.isEmpty())
            return;

        loginButton.setEnabled(false);
        controller.login(user, pass, new ClientController.ResponseCallback() {
            @Override
            public void onSuccess(String message, Object data) {
                loginButton.setEnabled(true);
                dispose();
                new MainFrame(controller).setVisible(true);
            }

            @Override
            public void onError(String message) {
                loginButton.setEnabled(true);
                JOptionPane.showMessageDialog(LoginFrame.this,
                        ResourceManager.get("error.login.failed") + "\n" + message,
                        ResourceManager.get("dialog.error.title"), JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void handleRegister() {
        String user = usernameField.getText();
        String pass = new String(passwordField.getPassword());
        if (user.isEmpty() || pass.isEmpty())
            return;

        registerButton.setEnabled(false);
        controller.register(user, pass, new ClientController.ResponseCallback() {
            @Override
            public void onSuccess(String message, Object data) {
                registerButton.setEnabled(true);
                JOptionPane.showMessageDialog(LoginFrame.this, ResourceManager.get("dialog.success.title"),
                        ResourceManager.get("dialog.success.title"), JOptionPane.INFORMATION_MESSAGE);
            }

            @Override
            public void onError(String message) {
                registerButton.setEnabled(true);
                JOptionPane.showMessageDialog(LoginFrame.this,
                        ResourceManager.get("error.register.failed") + "\n" + message,
                        ResourceManager.get("dialog.error.title"), JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}

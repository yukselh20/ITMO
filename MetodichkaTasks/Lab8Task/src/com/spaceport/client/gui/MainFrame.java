package com.spaceport.client.gui;

import com.spaceport.client.controller.ClientController;
import com.spaceport.client.util.ResourceManager;
import com.spaceport.common.Spaceship;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainFrame extends JFrame {
    private final ClientController controller;
    private final ShipTableModel tableModel;
    private final HoloMapPanel holoMapPanel;
    private List<Spaceship> allShips = new ArrayList<>();
    private JTextField filterField;
    private JLabel statusLabel;

    public MainFrame(ClientController controller) {
        this.controller = controller;
        this.tableModel = new ShipTableModel();
        this.holoMapPanel = new HoloMapPanel();

        setTitle(ResourceManager.get("dashboard.title") + " - " + controller.getCurrentUsername());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        initComponents();
        refreshData();
    }

    private void initComponents() {
        // Top Toolbar
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        JButton addButton = new JButton(ResourceManager.get("dashboard.add"));
        addButton.addActionListener(e -> showAddShipDialog());
        toolBar.add(addButton);

        JButton removeButton = new JButton(ResourceManager.get("dashboard.remove"));
        removeButton.addActionListener(e -> handleRemoveShip());
        toolBar.add(removeButton);

        toolBar.addSeparator();

        JButton refreshButton = new JButton(ResourceManager.get("dashboard.refresh"));
        refreshButton.addActionListener(e -> refreshData());
        toolBar.add(refreshButton);

        toolBar.add(Box.createHorizontalGlue());

        JLabel filterLabel = new JLabel(ResourceManager.get("dashboard.filter"));
        toolBar.add(filterLabel);

        filterField = new JTextField(15);
        filterField.addActionListener(e -> applyFilter());
        // Add document listener for real-time filtering if desired, but action listener
        // is simpler
        toolBar.add(filterField);

        JButton sortButton = new JButton("Sort");
        sortButton.addActionListener(e -> handleSort());
        toolBar.add(sortButton);

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> handleClear());
        toolBar.add(clearButton);

        JButton helpButton = new JButton("Help");
        helpButton.addActionListener(e -> handleHelp());
        toolBar.add(helpButton);

        toolBar.addSeparator();

        JButton logoutButton = new JButton(ResourceManager.get("dashboard.logout"));
        logoutButton.addActionListener(e -> handleLogout());
        toolBar.add(logoutButton);

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> handleExit());
        toolBar.add(exitButton);

        add(toolBar, BorderLayout.NORTH);

        // Split Pane (Table vs Map)
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        // Left: Table
        JTable table = new JTable(tableModel);
        splitPane.setLeftComponent(new JScrollPane(table));

        // Right: Map
        splitPane.setRightComponent(holoMapPanel);
        splitPane.setDividerLocation(500);
        splitPane.setResizeWeight(0.5);

        add(splitPane, BorderLayout.CENTER);

        // Status Bar
        statusLabel = new JLabel("Ready");
        add(statusLabel, BorderLayout.SOUTH);
    }

    private void refreshData() {
        statusLabel.setText("Refreshing...");
        controller.getShips(new ClientController.ResponseCallback() {
            @Override
            @SuppressWarnings("unchecked")
            public void onSuccess(String message, Object data) {
                if (data instanceof List) {
                    allShips = (List<Spaceship>) data;
                    applyFilter();
                    statusLabel.setText("Data loaded: " + allShips.size() + " ships.");
                }
            }

            @Override
            public void onError(String message) {
                statusLabel.setText("Error: " + message);
            }
        });
    }

    private void applyFilter() {
        String query = filterField.getText().toLowerCase();
        List<Spaceship> filtered = allShips.stream()
                .filter(s -> s.getName().toLowerCase().contains(query) || s.getType().toLowerCase().contains(query))
                .collect(Collectors.toList());

        tableModel.setShips(filtered);
        holoMapPanel.setShips(filtered, controller.getCurrentUserId());
    }

    private void showAddShipDialog() {
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField tonnageField = new JTextField();
        JTextField xField = new JTextField();
        JTextField yField = new JTextField();

        Object[] message = {
                "ID:", idField,
                "Name:", nameField,
                "Tonnage:", tonnageField,
                "X (Limit: -1000 to +1000):", xField,
                "Y (Limit: -1000 to +1000):", yField
        };

        int option = JOptionPane.showConfirmDialog(this, message, ResourceManager.get("dashboard.add"),
                JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String id = idField.getText();
                String name = nameField.getText();
                double tonnage = Double.parseDouble(tonnageField.getText());
                double x = Double.parseDouble(xField.getText());
                double y = Double.parseDouble(yField.getText());

                if (Math.abs(x) > 1000 || Math.abs(y) > 1000) {
                    throw new IllegalArgumentException("Coordinates must be between -1000 and 1000.");
                }

                Spaceship ship = new Spaceship(id, name, tonnage, "Cargo", 0, x, y);
                controller.dockShip(ship, new ClientController.ResponseCallback() {
                    @Override
                    public void onSuccess(String msg, Object data) {
                        JOptionPane.showMessageDialog(MainFrame.this, msg);
                        refreshData();
                    }

                    @Override
                    public void onError(String msg) {
                        JOptionPane.showMessageDialog(MainFrame.this, msg, ResourceManager.get("dialog.error.title"),
                                JOptionPane.ERROR_MESSAGE);
                    }
                });

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Invalid input: " + e.getMessage());
            }
        }
    }

    private void handleRemoveShip() {
        String id = JOptionPane.showInputDialog(this, "Enter Ship ID to undock:");
        if (id != null && !id.isBlank()) {
            controller.undockShip(id, new ClientController.ResponseCallback() {
                @Override
                public void onSuccess(String msg, Object data) {
                    JOptionPane.showMessageDialog(MainFrame.this, msg);
                    refreshData();
                }

                @Override
                public void onError(String msg) {
                    JOptionPane.showMessageDialog(MainFrame.this, msg, ResourceManager.get("dialog.error.title"),
                            JOptionPane.ERROR_MESSAGE);
                }
            });
        }
    }

    private void handleLogout() {
        controller.logout();
        dispose();
        new LoginFrame(controller).setVisible(true);
    }

    private void handleSort() {
        controller.sortShips(new ClientController.ResponseCallback() {
            @Override
            public void onSuccess(String msg, Object data) {
                if (data instanceof List) {
                    allShips = (List<Spaceship>) data;
                    applyFilter();
                    JOptionPane.showMessageDialog(MainFrame.this, msg);
                }
            }

            @Override
            public void onError(String msg) {
                JOptionPane.showMessageDialog(MainFrame.this, msg, ResourceManager.get("dialog.error.title"),
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void handleClear() {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to clear all your ships?",
                "Confirm Clear", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            controller.clearShips(new ClientController.ResponseCallback() {
                @Override
                public void onSuccess(String msg, Object data) {
                    JOptionPane.showMessageDialog(MainFrame.this, msg);
                    refreshData();
                }

                @Override
                public void onError(String msg) {
                    JOptionPane.showMessageDialog(MainFrame.this, msg, ResourceManager.get("dialog.error.title"),
                            JOptionPane.ERROR_MESSAGE);
                }
            });
        }
    }

    private void handleHelp() {
        controller.getHelp(new ClientController.ResponseCallback() {
            @Override
            public void onSuccess(String msg, Object data) {
                JTextArea textArea = new JTextArea(msg);
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(400, 300));
                JOptionPane.showMessageDialog(MainFrame.this, scrollPane, "Help", JOptionPane.INFORMATION_MESSAGE);
            }

            @Override
            public void onError(String msg) {
                JOptionPane.showMessageDialog(MainFrame.this, msg, ResourceManager.get("dialog.error.title"),
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void handleExit() {
        controller.exitApp();
        System.exit(0);
    }
}

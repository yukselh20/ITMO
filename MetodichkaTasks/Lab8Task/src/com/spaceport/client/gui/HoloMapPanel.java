package com.spaceport.client.gui;

import com.spaceport.common.Spaceship;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HoloMapPanel extends JPanel {
    private List<Spaceship> ships = new ArrayList<>();
    private int currentUserId;
    private final Map<String, Float> animationScales = new ConcurrentHashMap<>();
    private final Timer animationTimer;

    public HoloMapPanel() {
        setBackground(Color.BLACK);
        // Animation loop
        animationTimer = new Timer(30, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean needsRepaint = false;
                for (String id : animationScales.keySet()) {
                    float scale = animationScales.get(id);
                    if (scale < 1.0f) {
                        scale += 0.05f;
                        if (scale > 1.0f)
                            scale = 1.0f;
                        animationScales.put(id, scale);
                        needsRepaint = true;
                    }
                }
                if (needsRepaint) {
                    repaint();
                }
            }
        });
        animationTimer.start();
    }

    public void setShips(List<Spaceship> ships, int currentUserId) {
        this.ships = ships;
        this.currentUserId = currentUserId;

        // Initialize animation for new ships
        for (Spaceship ship : ships) {
            animationScales.putIfAbsent(ship.getId(), 0.0f);
        }
        // Remove old animation entries
        animationScales.keySet().removeIf(id -> ships.stream().noneMatch(s -> s.getId().equals(id)));

        repaint();
    }

    private static final double MAX_COORD = 1000.0;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int cx = w / 2;
        int cy = h / 2;

        // Calculate scale to fit MAX_COORD with some padding (90% of screen)
        double scale = Math.min(w, h) / 2.0 / MAX_COORD * 0.9;

        // Draw grid relative to center
        g2d.setColor(Color.DARK_GRAY);
        // Vertical lines
        for (double x = 0; x <= MAX_COORD; x += 250) {
            int px = (int) (x * scale);
            g2d.drawLine(cx + px, 0, cx + px, h);
            g2d.drawLine(cx - px, 0, cx - px, h);
        }
        // Horizontal lines
        for (double y = 0; y <= MAX_COORD; y += 250) {
            int py = (int) (y * scale);
            g2d.drawLine(0, cy + py, w, cy + py);
            g2d.drawLine(0, cy - py, w, cy - py);
        }

        // Draw Axes
        g2d.setColor(Color.GRAY);
        g2d.drawLine(cx, 0, cx, h); // Y Axis
        g2d.drawLine(0, cy, w, cy); // X Axis

        // Draw Limit Labels
        g2d.setColor(Color.YELLOW);
        g2d.setFont(new Font("Arial", Font.BOLD, 12));

        // X Axis Limits
        g2d.drawString(String.valueOf((int) MAX_COORD), (int) (cx + MAX_COORD * scale) - 20, cy + 15);
        g2d.drawString(String.valueOf((int) -MAX_COORD), (int) (cx - MAX_COORD * scale) - 10, cy + 15);

        // Y Axis Limits
        g2d.drawString(String.valueOf((int) MAX_COORD), cx + 5, (int) (cy - MAX_COORD * scale) + 10);
        g2d.drawString(String.valueOf((int) -MAX_COORD), cx + 5, (int) (cy + MAX_COORD * scale) - 5);

        // Draw ships
        for (Spaceship ship : ships) {
            double x = ship.getX();
            double y = ship.getY();

            // Transform to screen coordinates with scale
            int screenX = (int) (cx + x * scale);
            int screenY = (int) (cy - y * scale);

            // Size based on tonnage (min 10, max 50) - Scale size slightly too? Maybe not
            // necessary.
            int size = (int) Math.max(10, Math.min(50, ship.getTonnage() / 10));

            // Apply animation scale
            float animScale = animationScales.getOrDefault(ship.getId(), 1.0f);
            int animatedSize = (int) (size * animScale);

            // Color based on owner
            if (ship.getOwnerId() == currentUserId) {
                g2d.setColor(Color.GREEN);
            } else {
                g2d.setColor(Color.RED);
            }

            g2d.fillOval(screenX - animatedSize / 2, screenY - animatedSize / 2, animatedSize, animatedSize);

            // Draw Label
            g2d.setColor(Color.WHITE);
            g2d.drawString(ship.getName(), screenX + size / 2, screenY);
        }
    }
}

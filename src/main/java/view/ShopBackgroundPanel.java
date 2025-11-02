package view;

import controller.GameState;
import game.ArkanoidGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ShopBackgroundPanel extends JPanel {
    private final ArkanoidGame game;
    private final String[] backgrounds = {
            "/images/backgrounds/background2.png",
            "/images/backgrounds/background3.png",
            "/images/backgrounds/background4.png",
            "/images/backgrounds/background5.png",
            "/images/backgrounds/background6.png",
            "/images/backgrounds/background7.png",
            "/images/backgrounds/background8.png",
            "/images/backgrounds/background9.png"
    };

    public ShopBackgroundPanel(ArkanoidGame game) {
        this.game = game;
        setLayout(null);
        setBackground(Color.BLACK);
        initUI();
    }

    private void initUI() {
        JLabel lblTitle = new JLabel("SELECT BACKGROUND", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 48));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(0, 20, 1200, 50);
        add(lblTitle);

        JPanel grid = new JPanel(new GridLayout(2, 4, 20, 20));
        grid.setBounds(100, 100, 1000, 400);
        grid.setOpaque(false);
        add(grid);

        for (String path : backgrounds) {
            ImageIcon icon = new ImageIcon(getClass().getResource(path));
            Image scaled = icon.getImage().getScaledInstance(240, 130, Image.SCALE_SMOOTH);
            JLabel label = new JLabel(new ImageIcon(scaled));
            label.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));

            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    GameBackground.setCurrentBackground(path);
                    JOptionPane.showMessageDialog(ShopBackgroundPanel.this,
                            "Selected new background!");
                    game.changeState(GameState.SHOP);
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    label.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    label.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
                }
            });

            grid.add(label);
        }

        JButton btnBack = new JButton("BACK");
        btnBack.setBounds(500, 550, 160, 60);
        btnBack.addActionListener(e -> game.changeState(GameState.SHOP));
        add(btnBack);
    }
}

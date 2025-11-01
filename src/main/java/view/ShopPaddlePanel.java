package view;

import controller.GameState;
import game.ArkanoidGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ShopPaddlePanel extends JPanel {
    private final ArkanoidGame game;

    private final String[] paddles = {
            "/images/paddles/normal/NormalPaddle_short.png",
            "/images/paddles/normal/NormalPaddle_default.png",
            "/images/paddles/normal/NormalPaddle_long.png"
    };

    public ShopPaddlePanel(ArkanoidGame game) {
        this.game = game;
        setLayout(null);
        setBackground(Color.BLACK);
        initUI();
    }

    private void initUI() {
        JLabel lblTitle = new JLabel("SELECT PADDLE", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 48));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(0, 20, 1200, 50);
        add(lblTitle);

        JPanel grid = new JPanel(new GridLayout(1, 3, 40, 20));
        grid.setBounds(150, 200, 900, 200);
        grid.setOpaque(false);
        add(grid);

        for (String path : paddles) {
            ImageIcon icon = new ImageIcon(getClass().getResource(path));
            Image scaled = icon.getImage().getScaledInstance(240, 60, Image.SCALE_SMOOTH);
            JLabel label = new JLabel(new ImageIcon(scaled));
            label.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));

            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    PaddleAssets.setCurrentPaddle(path);
                    JOptionPane.showMessageDialog(ShopPaddlePanel.this,
                            "Selected new paddle!");
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
        btnBack.setBounds(520, 500, 160, 40);
        btnBack.addActionListener(e -> game.changeState(GameState.SHOP));
        add(btnBack);
    }
}

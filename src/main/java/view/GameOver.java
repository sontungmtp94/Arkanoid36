package view;


import controller.GameManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Lớp GameOver hiển thị thông báo "Game Over" và hướng dẫn restart.
 */
public class GameOver extends JPanel {

    public GameOver(int panelWidth, int panelHeight, GameManager gameManager) {
        setLayout(null);
        setOpaque(false);

        BufferedImage gameOverImage = SpritesView.loadSprite("images/utils/game_over.png");
        BufferedImage mainMenuBtn = SpritesView.loadSprite("images/utils/main_menu.png");
        BufferedImage restartBtn = SpritesView.loadSprite("images/utils/restart.png");

        Image scaledLabel =  gameOverImage.getScaledInstance(panelWidth, -1, Image.SCALE_SMOOTH);
        Image scaledMainMenu = mainMenuBtn.getScaledInstance(panelWidth / 10, -1, Image.SCALE_SMOOTH);
        Image scaledRestart = restartBtn.getScaledInstance(panelWidth / 10, -1, Image.SCALE_SMOOTH);

        JLabel gameOverLabel = new JLabel(new ImageIcon(scaledLabel));
        JButton mainMenuButton = new JButton(new ImageIcon(scaledMainMenu));
        JButton restartButton = new JButton(new ImageIcon(scaledRestart));


        int labelHeight = scaledLabel.getHeight(null);
        int buttonWidth = scaledMainMenu.getWidth(null);
        int buttonHeight = scaledMainMenu.getHeight(null);


        int spacing = 60;

        int startX = (panelWidth - 2 * buttonWidth - spacing) / 2;


        gameOverLabel.setBounds(0, panelHeight / 2 - labelHeight, panelWidth, labelHeight);
        mainMenuButton.setBounds(startX, panelHeight / 2 + 20, buttonWidth, buttonHeight);
        restartButton.setBounds(startX + buttonWidth + spacing, panelHeight / 2 + 20, buttonWidth, buttonHeight);

        mainMenuButton.addActionListener(e -> {
            // Tạm thời chưa thêm.
        });

        restartButton.addActionListener(e -> {
            gameManager.restartGame();
        });


        add(gameOverLabel);
        add(mainMenuButton);
        add(restartButton);

        setVisible(false);
    }

    /** Hiển thị bảng Game Over. */
    public void showPanel() {
        setVisible(true);
        repaint();
    }

    /** Ẩn bảng Game Over. */
    public void hidePanel() {
        setVisible(false);
    }
}

package view;

import controller.GameManager;
import controller.GameState;
import controller.KeyManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

/**
 * Lớp LevelCompleted hiển thị thông báo "Level Completed" và hướng dẫn next level/ restart.
 */
public class LevelCompleted extends JPanel {

    public LevelCompleted(int panelWidth, int panelHeight, GameManager gameManager) {
        setLayout(null);

        setLayout(null);
        setOpaque(false);

        BufferedImage winningImage = SpritesView.loadSprite("images/utils/you_win.png");
        BufferedImage mainMenuBtn = SpritesView.loadSprite("images/utils/main_menu.png");
        BufferedImage restartBtn = SpritesView.loadSprite("images/utils/restart.png");
        BufferedImage nextLevelBtn = SpritesView.loadSprite("images/utils/next_level.png");

        Image scaledLabel =  winningImage.getScaledInstance(panelWidth, -1, Image.SCALE_SMOOTH);
        Image scaledMainMenu = mainMenuBtn.getScaledInstance(panelWidth / 10, -1, Image.SCALE_SMOOTH);
        Image scaledRestart = restartBtn.getScaledInstance(panelWidth / 10, -1, Image.SCALE_SMOOTH);
        Image scaledNextLevel = nextLevelBtn.getScaledInstance(panelWidth / 10, -1, Image.SCALE_SMOOTH);

        JLabel winningLabel = new JLabel(new ImageIcon(scaledLabel));
        JButton mainMenuButton = new JButton(new ImageIcon(scaledMainMenu));
        JButton restartButton = new JButton(new ImageIcon(scaledRestart));
        JButton nextLevelButton = new JButton(new ImageIcon(scaledNextLevel));


        int labelHeight = scaledLabel.getHeight(null);
        int buttonWidth = scaledMainMenu.getWidth(null);
        int buttonHeight = scaledMainMenu.getHeight(null);

        int spacing = 60;

        int startX = (panelWidth - 3 * buttonWidth - 2 * spacing) / 2;




        winningLabel.setBounds(0, panelHeight / 2 - labelHeight, panelWidth, labelHeight);
        mainMenuButton.setBounds(startX, panelHeight / 2 + 20, buttonWidth, buttonHeight);
        restartButton.setBounds(startX + buttonWidth + spacing, panelHeight / 2 + 20, buttonWidth, buttonHeight);
        nextLevelButton.setBounds(startX + 2 * (buttonWidth + spacing), panelHeight / 2 + 20, buttonWidth, buttonHeight);


        mainMenuButton.addActionListener(e -> {
             // Tạm thời chưa thêm.
        });

        restartButton.addActionListener(e -> {
            gameManager.restartGame();
        });

        nextLevelButton.addActionListener(e -> {
            gameManager.nextLevel();
        });

        add(winningLabel);
        add(mainMenuButton);
        add(restartButton);
        add(nextLevelButton);

        setVisible(false);

    }




    /** Hiển thị bảng Level Completed. */
    public void showPanel() {
        setVisible(true);
        repaint();
    }

    /** Ẩn bảng Level Completed. */
    public void hidePanel() {
        setVisible(false);
    }
}

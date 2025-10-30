package view;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import controller.GameManager;
import audio.SoundManager;
import audio.SoundId;
import controller.GameState;
import game.ArkanoidGame;

/**
 * Lớp LevelCompleted hiển thị thông báo "Level Completed" và hướng dẫn next level/ restart.
 */
public class LevelCompleted extends JPanel {
    private final GameManager gameManager;

    public LevelCompleted(int panelWidth, int panelHeight, GameManager gameManager) {
        this.gameManager = gameManager;

        setLayout(null);
        setOpaque(false);

        BufferedImage winningImage = SpritesView.loadSprite("images/utils/you_win.png");
        BufferedImage mainMenuBtn = SpritesView.loadSprite("images/utils/main_menu.png");
        BufferedImage restartBtn = SpritesView.loadSprite("images/utils/restart.png");
        BufferedImage nextLevelBtn = SpritesView.loadSprite("images/utils/next_level.png");

        Image scaledLabel = winningImage.getScaledInstance(panelWidth, -1, Image.SCALE_SMOOTH);
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
            SoundManager.get().playSfx(SoundId.SFX_CLICK);
            ((ArkanoidGame) SwingUtilities.getWindowAncestor(this)).changeState(GameState.MENU);
        });

        restartButton.addActionListener(e -> {
            SoundManager.get().playSfx(SoundId.SFX_CLICK);
            gameManager.restartGame();
        });

        nextLevelButton.addActionListener(e -> {
            SoundManager.get().playSfx(SoundId.SFX_CLICK);
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
        SoundManager.get().playSfx(SoundId.SFX_WIN);
    }

    /** Ẩn bảng Level Completed. */
    public void hidePanel() {
        setVisible(false);
    }
}

package view;

import controller.GameManager;
import controller.GameState;
import game.ArkanoidGame;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import audio.SoundManager;
import audio.SoundId;


/**
 * Lớp GameOver hiển thị thông báo "Game Over" và hướng dẫn restart.
 */
public class Paused extends JPanel {
    private final GameManager gameManager;

    public Paused(int panelWidth, int panelHeight, GameManager gameManager) {
        this.gameManager = gameManager;

        setLayout(null);
        setOpaque(false);

        BufferedImage pausedImage = SpritesView.loadSprite("images/utils/paused.png");
        BufferedImage mainMenuBtn = SpritesView.loadSprite("images/utils/main_menu.png");
        BufferedImage restartBtn = SpritesView.loadSprite("images/utils/restart.png");

        Image scaledLabel =  pausedImage.getScaledInstance(panelWidth, -1, Image.SCALE_SMOOTH);
        Image scaledMainMenu = mainMenuBtn.getScaledInstance(panelWidth / 10, -1, Image.SCALE_SMOOTH);
        Image scaledRestart = restartBtn.getScaledInstance(panelWidth / 10, -1, Image.SCALE_SMOOTH);

        JLabel pausedLabel = new JLabel(new ImageIcon(scaledLabel));
        JButton mainMenuButton = new JButton(new ImageIcon(scaledMainMenu));
        JButton restartButton = new JButton(new ImageIcon(scaledRestart));


        int labelHeight = scaledLabel.getHeight(null);
        int buttonWidth = scaledMainMenu.getWidth(null);
        int buttonHeight = scaledMainMenu.getHeight(null);


        int spacing = 60;

        int startX = (panelWidth - 2 * buttonWidth - spacing) / 2;


        pausedLabel.setBounds(0, panelHeight / 2 - labelHeight, panelWidth, labelHeight);
        mainMenuButton.setBounds(startX, panelHeight / 2 + 20, buttonWidth, buttonHeight);
        restartButton.setBounds(startX + buttonWidth + spacing, panelHeight / 2 + 20, buttonWidth, buttonHeight);

        mainMenuButton.addActionListener(e -> {
            SoundManager.get().playSfx(SoundId.SFX_CLICK);
            hidePanel();
            gameManager.stopGame(); // Dừng game cũ trước khi chuyển.
            ((ArkanoidGame) SwingUtilities.getWindowAncestor(this)).changeState(GameState.MENU);
        });

        restartButton.addActionListener(e -> {
            SoundManager.get().playSfx(SoundId.SFX_CLICK);
            gameManager.restartGame();
            hidePanel();
        });


        add(pausedLabel);
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

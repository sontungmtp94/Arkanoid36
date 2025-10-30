package game;

import controller.GameState;
import controller.GameManager;
import view.*;
import audio.SoundManager;
import audio.SoundId;

import javax.swing.*;
import java.awt.*;

/**
 * Lớp ArkanoidGame là cửa sổ chính của trò chơi.
 */
public class ArkanoidGame extends JFrame {

    protected static final int WIDTH = 1200;
    protected static final int HEIGHT = 650;

    private GameState currentState;
    private JPanel currentPanel;

    public ArkanoidGame() {
        setTitle("Arkanoid36");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Mở màn hình chính
        changeState(GameState.MENU);

        setVisible(true);
    }

    public static int getGameWidth() {
        return WIDTH;
    }

    public static int getGameHeight() {
        return HEIGHT;
    }

    /** Chuyển đổi giữa các trạng thái (màn hình) của game */
    public void changeState(GameState newState) {
        this.currentState = newState;

        // Gỡ panel cũ
        if (currentPanel != null) {
            getContentPane().remove(currentPanel);
        }

        // Tạo panel mới dựa trên trạng thái
        switch (newState) {
            case MENU -> {
                SoundManager.get().playBgm(SoundId.BGM_MENU, 1200);
                currentPanel = new MainMenu(this);
            }
            case READY, PLAYING -> {
                SoundManager.get().playBgm(SoundId.BGM_GAME, 1200);
                currentPanel = new GameManager(WIDTH, HEIGHT, this);
            }
            case LEVEL_COMPLETED -> {
                SoundManager.get().playSfx(SoundId.SFX_WIN);
                currentPanel = new LevelCompleted(WIDTH, HEIGHT, null);
            }
            case GAME_OVER -> {
                SoundManager.get().playSfx(SoundId.SFX_LOSE);
                currentPanel = new GameOver(WIDTH, HEIGHT, null);
            }
            case PAUSE -> {
                currentPanel = createPausePanel();
            }
        }

        // Gắn panel mới vào cửa sổ.
        currentPanel.setPreferredSize(new Dimension(WIDTH,HEIGHT));
        setContentPane(currentPanel);
        pack();
        revalidate();
        repaint();
        currentPanel.requestFocusInWindow();
    }

    /** Tạo panel cho màn hình Pause */
    private JPanel createPausePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(0, 0, 0, 160));

        JLabel lbl = new JLabel("PAUSED", SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 40));
        lbl.setForeground(Color.WHITE);

        panel.add(lbl, BorderLayout.CENTER);
        return panel;
    }

    /** Lấy trạng thái hiện tại */
    public GameState getCurrentState() {
        return currentState;
    }

    public static void main(String[] args) {
        new ArkanoidGame();
    }
}

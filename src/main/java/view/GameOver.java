package view;

import javax.swing.*;
import java.awt.*;

/**
 * Lớp GameOver hiển thị thông báo "Game Over" và hướng dẫn restart.
 */
public class GameOver extends JPanel {

    private JLabel gameOverLabel;
    private JLabel restartLabel;

    public GameOver(int panelWidth, int panelHeight) {
        setLayout(null);

        gameOverLabel = new JLabel("Game Over!");
        gameOverLabel.setFont(new Font("Arial", Font.BOLD, 72));
        gameOverLabel.setForeground(Color.RED);
        gameOverLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gameOverLabel.setBounds(0, panelHeight / 2 - 120, panelWidth, 100);
        add(gameOverLabel);

        restartLabel = new JLabel("Press R to restart.");
        restartLabel.setFont(new Font("Arial", Font.PLAIN, 30));
        restartLabel.setForeground(Color.DARK_GRAY);
        restartLabel.setHorizontalAlignment(SwingConstants.CENTER);
        restartLabel.setBounds(0, panelHeight / 2 - 30, panelWidth, 60);
        add(restartLabel);


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

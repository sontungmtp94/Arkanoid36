package view;

import javax.swing.*;
import java.awt.*;

/**
 * Lớp LevelCompleted hiển thị thông báo "Level Completed" và hướng dẫn next level/ restart.
 */
public class LevelCompleted extends JPanel {

    public LevelCompleted(int panelWidth, int panelHeight) {
        setLayout(null);

        JLabel winningLabel;
        JLabel nextLevelLabel;
        JLabel restartLabel;

        winningLabel = new JLabel("You Win!");
        winningLabel.setFont(new Font("Arial", Font.BOLD, 72));
        winningLabel.setForeground(Color.GREEN);
        winningLabel.setHorizontalAlignment(SwingConstants.CENTER);
        winningLabel.setBounds(0, panelHeight / 2 - 120, panelWidth, 100);
        add(winningLabel);

        nextLevelLabel = new JLabel("Press N to go to the next level.");
        nextLevelLabel.setFont(new Font("Arial", Font.PLAIN, 30));
        nextLevelLabel.setForeground(Color.DARK_GRAY);
        nextLevelLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nextLevelLabel.setBounds(0, panelHeight / 2 - 40, panelWidth, 60);
        add(nextLevelLabel);

        restartLabel = new JLabel("Press R to restart.");
        restartLabel.setFont(new Font("Arial", Font.PLAIN, 30));
        restartLabel.setForeground(Color.DARK_GRAY);
        restartLabel.setHorizontalAlignment(SwingConstants.CENTER);
        restartLabel.setBounds(0, panelHeight / 2 - 10, panelWidth, 60);
        add(restartLabel);


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

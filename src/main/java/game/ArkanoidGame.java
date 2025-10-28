package game;

import javax.swing.*;

/**
 * Lớp ArkanoidGame là cửa sổ chính của trò chơi.
 * Giờ đây không cần implements ScreenSwitcher nữa — enum ScreenSwitcher sẽ xử lý việc chuyển đổi.
 */
public class ArkanoidGame extends JFrame {

    protected static final int WIDTH = 1200;
    protected static final int HEIGHT = 650;

    public ArkanoidGame() {
        setTitle("Arkanoid36");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Mở màn hình chính
        ScreenSwitcher.MAIN_MENU.show(this);

        setVisible(true);
    }

    public static int getGameWidth() {
        return WIDTH;
    }

    public static int getGameHeight() {
        return HEIGHT;
    }

    public static void main(String[] args) {
        new ArkanoidGame();
    }
}

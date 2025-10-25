package game;

import javax.swing.*;
import java.awt.*;
import view.MainMenu;
import controller.GameManager;

/**
 * Lớp ArkanoidGame là lớp chính của trò chơi, kế thừa JFrame
 * và triển khai (implements) giao diện ScreenSwitcher.
 * Nhiệm vụ:
 *  - Quản lý cửa sổ chính của game.
 *  - Chuyển đổi giữa các màn hình như Menu và Game.
 */
public class ArkanoidGame extends JFrame implements ScreenSwitcher {

    /** Chiều rộng cửa sổ game. */
    protected static final int WIDTH = 1200;

    /** Chiều cao cửa sổ game. */
    protected static final int HEIGHT = 650;

    /** Thành phần giao diện hiện tại đang hiển thị (view.MainMenu, GamePanel, ...). */
    private JPanel currentPanel;

    /**
     * Constructor khởi tạo trò chơi Arkanoid.
     * Thiết lập kích thước cửa sổ, tiêu đề và hiển thị menu chính.
     */
    public ArkanoidGame() {
        setTitle("Arkanoid36");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Căn giữa màn hình
        showMainMenu();              // Hiển thị menu chính khi khởi động
        setVisible(true);
    }

    /**
     * Hiển thị màn hình menu chính.
     * Nếu có panel cũ, nó sẽ bị gỡ bỏ trước khi thêm panel mới.
     */
    @Override
    public void showMainMenu() {
        if (currentPanel != null) {
            remove(currentPanel);
        }
        currentPanel = new MainMenu(this); // Truyền this để view.MainMenu có thể gọi lại showGame()
        add(currentPanel);
        revalidate();
        repaint();
    }

    /**
     * Hiển thị màn hình trò chơi.
     *
     */
    @Override
    public void showGame() {
        if (currentPanel != null) {
            remove(currentPanel);
        }

        // Tạo controller.GameManager (màn chơi chính)
        currentPanel = new GameManager(WIDTH, HEIGHT, this);
        currentPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        getContentPane().add(currentPanel);
        pack();

        add(currentPanel);

        // Đảm bảo focus để nhận bàn phím
        currentPanel.requestFocusInWindow();

        revalidate();
        repaint();
    }

    /**
     * Phương thức main() là điểm bắt đầu của chương trình.
     * Tạo một đối tượng ArkanoidGame để khởi động ứng dụng.
     */
    public static void main(String[] args) {
        new ArkanoidGame();
    }

    // Các getter và setter

    public static int getGameWidth() {
        return WIDTH;
    }

    public static int getGameHeight() {
        return HEIGHT;
    }
}

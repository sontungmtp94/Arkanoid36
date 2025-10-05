import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Lớp MainMenu đại diện cho màn hình menu chính của trò chơi.
 * Kế thừa JPanel để có thể hiển thị trên cửa sổ JFrame.
 * Nhiệm vụ:
 *  - Hiển thị các nút điều khiển (Play, Exit, ...).
 *  - Gọi các phương thức của ScreenSwitcher để đổi màn hình.
 */
public class MainMenu extends JPanel {

    /** Đối tượng dùng để chuyển đổi màn hình (được truyền từ ArkanoidGame). */
    private ScreenSwitcher screenSwitcher;

    /**
     * Constructor khởi tạo menu chính.
     *
     * @param switcher đối tượng ScreenSwitcher để chuyển đổi giữa các màn hình.
     */
    public MainMenu(ScreenSwitcher switcher) {
        this.screenSwitcher = switcher;
        setLayout(new GridBagLayout());
        setBackground(Color.BLACK);

        // Nút "Play" để bắt đầu trò chơi
        JButton playButton = new JButton("Play");
        styleButton(playButton);
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                screenSwitcher.showGame(); // Gọi phương thức hiển thị game
            }
        });

        // Nút "Exit" để thoát trò chơi
        JButton exitButton = new JButton("Exit");
        styleButton(exitButton);
        exitButton.addActionListener(e -> System.exit(0));

        // Panel chứa các nút
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false); // Làm trong suốt nền panel con
        buttonPanel.setLayout(new GridLayout(2, 1, 0, 20));
        buttonPanel.add(playButton);
        buttonPanel.add(exitButton);

        add(buttonPanel);
    }

    /**
     * Định dạng giao diện cho nút (button).
     *
     * @param b nút cần định dạng.
     */
    public void styleButton(JButton b) {
        b.setFont(new Font("Arial", Font.BOLD, 24));
        b.setForeground(Color.WHITE);
        b.setBackground(Color.DARK_GRAY);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
    }
}

package view;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Quản lý background hiện tại của trò chơi.
 * Cho phép đổi ảnh nền từ ShopPanel hoặc các trạng thái khác.
 */
public class GameBackground {

    private static String currentBackgroundPath = "/images/utils/background_menu.png";
    private static BufferedImage currentBackground;

    static {
        loadCurrentBackground();
    }

    /** Load ảnh nền hiện tại */
    private static void loadCurrentBackground() {
        try {
            currentBackground = ImageIO.read(GameBackground.class.getResourceAsStream(currentBackgroundPath));
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Không thể load background: " + currentBackgroundPath);
            currentBackground = null;
        }
    }

    /** Đổi ảnh nền sang đường dẫn mới */
    public static void setCurrentBackground(String newPath) {
        currentBackgroundPath = newPath;
        loadCurrentBackground();
    }

    /** Lấy ảnh nền hiện tại */
    public static BufferedImage getBackground() {
        return currentBackground;
    }

    /** Lấy đường dẫn hiện tại */
    public static String getCurrentBackgroundPath() {
        return currentBackgroundPath;
    }
}

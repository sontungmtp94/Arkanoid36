package view;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * SpritesView - Quản lý và tải toàn bộ sprites trong game.
 * Dùng cho Brick, Ball, Paddle, PowerUp, Background, v.v.
 */
public class SpritesView {

    // Cache lưu trữ các ảnh đã load (để tránh đọc file nhiều lần)
    private static final HashMap<String, BufferedImage> cache = new HashMap<>();

    /**
     * @param path Đường dẫn tương đối đến ảnh (ví dụ: "images/brick/bricks.png")
     * @return Ảnh BufferedImage hoặc null nếu lỗi.
     */
    public static BufferedImage loadSprite(String path) {
        // Nếu ảnh đã có trong cache thì dùng lại
        if (cache.containsKey(path)) {
            return cache.get(path);
        }

        try {
            BufferedImage img = ImageIO.read(new File("src/main/resources/" + path));
            cache.put(path, img);
            return img;
        } catch (IOException e) {
            System.err.println("Failed to load sprite: " + path + " | " + e.getMessage());
            return null;
        }
    }

    /**
     * Cắt một vùng ảnh từ spritesheet (dùng cho Brick, animation, ...).
     */
    public static BufferedImage cutSprite(BufferedImage sheet, int x, int y, int width, int height) {
        return sheet.getSubimage(x, y, width, height);
    }
}

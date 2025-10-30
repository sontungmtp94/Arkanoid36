package model.brick;

import view.SpritesView;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * BrickType đại diện cho 6 loại gạch.
 * Mỗi loại có tối đa 6 hp (trừ metal).
 */
public enum BrickType {
    RED(0, 6),
    CYAN(1, 5),
    BLUE(2, 4),
    PURPLE(3, 3),
    YELLOW(4, 2),
    ORANGE(5, 1),
    METAL(6, 100);

    private static final int WIDTH = 32;
    private static final int HEIGHT = 16;

    private final int row;  // Vị trí hàng trong sprite sheet
    private final int hitPoints;  // HP của loại gạch này
    private final BufferedImage[] frames = new BufferedImage[6];  // Số trạng thái (HP) của gạch

    BrickType(int row, int hitPoints) {
        this.row = row;
        this.hitPoints = hitPoints;
    }

    static {
        BufferedImage sheet = SpritesView.loadSprite("images/brick/bricks.png");
        if (sheet != null) {
            for (BrickType type : values()) {
                for (int i = 0; i < 6; i++) {
                    type.frames[i] = SpritesView.cutSprite(
                            sheet,
                            i * WIDTH,         // cột = mức độ nứt
                            type.row * HEIGHT,    // hàng = màu
                            WIDTH,
                            HEIGHT
                    );
                }
            }
            System.out.println("Brick sprites loaded successfully!");
        } else {
            System.err.println("Failed to load bricks.png");
        }
    }

    /**
     * Lấy hình ảnh ứng với số HP còn lại.
     * HP càng thấp → gạch càng nứt.
     */
    public BufferedImage getFrame(int hpRemaining) {
        if (this == METAL) return frames[0];
        int damage = hitPoints - hpRemaining; // số lần bị trúng
        int index = Math.min(damage, frames.length - 1);
        return frames[index];
    }

    public int getHitPoints() {
        return hitPoints;
    }
}

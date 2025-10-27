package model.brick;

import view.SpritesView;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * BrickType đại diện cho 6 loại gạch.
 * Mỗi loại có 3 hp (3 chạm sẽ vỡ).
 */
public enum BrickType {
    RED(0, 3),
    CYAN(1, 2),
    BLUE(2, 1),
    PURPLE(3, 2),
    YELLOW(4, 1),
    ORANGE(5, 3);

    private static final int WIDTH = 32;
    private static final int HEIGHT = 16;

    private final int row;  // Vị trí hàng trong sprite sheet
    private final int hitPoints;  // HP của loại gạch này
    private final BufferedImage[] frames = new BufferedImage[3];  // Số trạng thái (HP) của gạch

    BrickType(int row, int hitPoints) {
        this.row = row;
        this.hitPoints = hitPoints;

    }

    static {
        BufferedImage sheet = SpritesView.loadSprite("images/brick/bricks2.png");
        if (sheet != null) {
            for (BrickType type : values()) {
                for (int i = 0; i < 3; i++) {
                    type.frames[i] = SpritesView.cutSprite(
                            sheet,
                            i * WIDTH,         // cột = mức độ nứt
                            type.row * HEIGHT, // hàng = màu
                            WIDTH,
                            HEIGHT
                    );
                }
            }
            System.out.println("Brick sprites loaded successfully!");
        } else {
            System.err.println("Failed to load bricks2.png");
        }
    }

    /**
     * Lấy hình ảnh ứng với số HP còn lại.
     * HP càng thấp → gạch càng nứt.
     */
    public BufferedImage getFrame(int hpRemaining) {
        int damageTaken = hitPoints - hpRemaining; // số lần bị trúng
        int index = Math.min(damageTaken, 2);      // frame 0→1→2
        return frames[index];
    }

    public int getHitPoints() {
        return hitPoints;
    }

    /** Trả về loại gạch ngẫu nhiên với độ cứng cố định */
    private static final Random RANDOM = new Random();

    public static BrickType getRandomType() {
        BrickType[] values = values();
        return values[RANDOM.nextInt(values.length)];
    }
}

package model.brick;

import view.SpritesView;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * BrickType đại diện cho các loại gạch.
 * Mỗi loại có vị trí riêng trên spritesheet bricks.png.
 */
public enum BrickType {
    RED(0, 0),
    CYAN(0, 16),
    BLUE(0, 32),
    PURPLE(0, 48),
    YELLOW(0, 64),
    ORANGE(0, 80);

    private static final int WIDTH = 32;
    private static final int HEIGHT = 16;
    private static final int HITPOINTS = 1;

    private int x, y;
    private BufferedImage sprite;

    // Constructor
    BrickType(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Static block được chạy SAU khi tất cả enum được tạo
    static {
        BufferedImage sheet = SpritesView.loadSprite("images/brick/bricks.png");
        for (BrickType type : values()) {
            if (sheet != null) {
                type.sprite = SpritesView.cutSprite(sheet, type.x, type.y, WIDTH, HEIGHT);
            }
        }
        System.out.println("Brick sprites loaded successfully!");
    }

    public BufferedImage getSprite() {
        return sprite;
    }

    public int getHitPoints() {
        return HITPOINTS;
    }

    /** Trả về 1 loại gạch ngẫu nhiên. */
    public static BrickType getRandomType() {
        BrickType[] values = values();
        return values[new Random().nextInt(values.length)];
    }
}

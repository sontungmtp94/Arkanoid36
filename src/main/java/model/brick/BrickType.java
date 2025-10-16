package model.brick;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * model.brick.BrickType đại diện cho các loại model.brick.Brick.
 * Mỗi loại có màu sắc và độ bền mặc định khác nhau.
 */
public enum BrickType {
    RED(0, 0, 32, 16, 1),
    CYAN(0, 16, 32, 16, 1),
    BLUE(0, 32, 32, 16, 1),
    PURPLE(0, 48, 32, 16, 1),
    YELLOW(0, 64, 32, 16, 1),
    ORANGE(0, 80, 32, 16, 1);

    private final int x, y, width, height;
    private final int hitPoints;
    private BufferedImage sprite;

    BrickType(int x, int y, int width, int height, int hitPoints) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.hitPoints = hitPoints;
    }

    // Getter cho spritesheet
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }

    // Getter cho gameplay
    public int getHitPoints() { return hitPoints; }


    // Sprite getter / setter
    public BufferedImage getSprite() { return sprite; }
    public void setSprite(BufferedImage sprite) { this.sprite = sprite; }

    /** Chọn ngẫu nhiên 1 loại Brick */
    public static BrickType getRandomType() {
        BrickType[] values = BrickType.values();
        return values[new Random().nextInt(values.length)];
    }
}

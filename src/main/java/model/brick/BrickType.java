package model.brick;

import java.awt.*;

/**
 * model.brick.BrickType đại diện cho các loại model.brick.Brick.
 * Mỗi loại có màu sắc và độ bền mặc định khác nhau.
 */
public enum BrickType {
    NORMAL(1, Color.CYAN),
    MEDIUM(2, Color.ORANGE),
    HARD(3, Color.RED);

    private final int hitPoints;
    private final Color color;

    BrickType(int hitPoints, Color color) {
        this.hitPoints = hitPoints;
        this.color = color;
    }

    // Các getter

    public int getHitPoints() {
        return hitPoints;
    }

    public Color getColor() {
        return color;
    }
}

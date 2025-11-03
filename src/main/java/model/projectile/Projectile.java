package model.projectile;

import model.base.MovableObject;

/**
 * Lớp trừu tượng Projecttile phục vụ cho các skill của các paddle đặc biệt.
 */
public abstract class Projectile extends MovableObject {
    protected boolean active = false; // Biến kiểm tra được kích hoạt chưa

    /**
     * Constructor cho Projectile.
     *
     * @param x      Tọa độ x
     * @param y      Tọa độ y
     * @param width  Chiều rộng
     * @param height Chiều cao
     */
    public Projectile(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    // Getter và setter.

    public boolean isActive() {
        return active;
    }
}

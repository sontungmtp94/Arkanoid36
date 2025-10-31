package model.projectile;

import model.base.MovableObject;

public abstract class Projectile extends MovableObject {
    protected boolean active = false;

    /**
     * Constructor cho Projectile.
     *
     * @param x      Tọa độ x, Chiều ngang từ Trái sang Phải
     * @param y      Tọa độ y, Chiều dọc từ Trên xuống Dưới
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

    public void setActive(boolean active) {
        this.active = active;
    }
}
